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
    var customerId: String? = null
    lateinit var customer: Customer

    fun addCustomer() {
        customer = Customer()
        var doc = dbBusiness.document()
        customerId = doc.id
        ctx.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).edit().apply { putString("customerId", customerId); commit() }
        doc.set(customer)
    }

    fun init(ctx: Activity, storeModel: StoreModel) {
        this.ctx = ctx
        this.storeModel = storeModel
        storeModel.customerModel = this
        customerId = ctx.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("customerId", null)
        if (customerId == null) addCustomer() else getCustomer(customerId!!)
    }

    private fun getCustomer(id: String) {
        dbBusiness.document(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    customer = document.toObject(Customer::class.java)!!
                    storeModel.actualStore = customer.actualStore
                } else {
                    Log.d("CustomerModel", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CustomerModel", "get failed with ", exception)
            }
    }

    fun updateCustomer(phone: String, mail: String, address: String) = dbBusiness.document(mail).update(mapOf("phone" to phone, "address" to address))

}