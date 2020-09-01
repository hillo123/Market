package com.dabyz.market.models

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

data class Customer(
    var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "",
    var actualStore: String? = null, var stores: ArrayList<String> = arrayListOf<String>("eamedina@gmail.com", "")
)// TODO actualStore and stores must be initialized empties and later filled whit the user interface

class CustomerModel : ViewModel() {
    private val dbBusiness = FirebaseFirestore.getInstance().collection("customers")
    lateinit var storeModel: StoreModel
    lateinit var ctx: Context
    var mail: String? = null
    var customer: Customer? = null

    fun addCustomer(customer: Customer) {
        this.customer = customer
        ctx.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).edit().apply { putString("customerMail", customer.mail); commit() }
        dbBusiness.document(customer.mail).set(customer)
        storeModel.actualStore = customer.actualStore
    }

    fun init(ctx: Activity, storeModel: StoreModel) {
        this.ctx = ctx
        this.storeModel = storeModel
        mail = ctx.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("customerMail", null)
        mail?.let { getCustomer(mail!!) }
    }

    private fun getCustomer(mail: String) {
        dbBusiness.document(mail).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    customer = document.toObject(Customer::class.java)
                    storeModel.actualStore = customer?.actualStore
                } else {
                    Log.d("CustomerModel", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CustomerModel", "get failed with ", exception)
            }
    }
}