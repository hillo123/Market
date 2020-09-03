package com.dabyz.market.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*
import kotlin.collections.ArrayList

data class Product(var title: String = "", var title2: String = "", var price: Long = 0, var photo: String = "", var fileName: String = "")
data class Business(
    var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "",
    var refs: ArrayList<Product> = ArrayList()
)

data class Line(val product: Product, var quantity: Int)
data class Order(val date: Date = Date(), var orderLines: ArrayList<Line> = ArrayList())
data class C2B(val customer: String = "", val business: String = "", var orders: ArrayList<Order> = ArrayList())

class StoreModel : ViewModel() {
    lateinit var customerModel: CustomerModel
    private var businessListener: ListenerRegistration? = null
    private var c2bListener: ListenerRegistration? = null
    private val dbBusiness = FirebaseFirestore.getInstance().collection("greengrocery")
    private val dbC2Bs = FirebaseFirestore.getInstance().collection("c2bs")

    var actualStore: String? = null
        set(value) {
            field = value;
            value?.let { initCurrentBusiness(value) }
        }

    val selectedBusiness = MutableLiveData<Business>()
    val selectedC2B = MutableLiveData<C2B>()

    private fun initCurrentBusiness(actualStore: String) {
        businessListener?.remove()
        businessListener = dbBusiness.document(actualStore).addSnapshotListener { snapshot, e ->
            e?.let { Log.w("Model", "Listen failed.", e); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                selectedBusiness.value = snapshot.toObject(Business::class.java)
            } else {
                Log.d("Model", "Current data: null")
            }
        }
        customerModel.customer?.let { initCurrentC2B(customerModel.customer!!, actualStore) }
    }

    private fun addC2B(c2b: C2B) = dbC2Bs.document(c2b.customer + "-" + c2b.business).set(c2b)

    private fun initCurrentC2B(customer: Customer, business: String) {
        // if Customer.stores not contain actualStore then create C2B and add to Customer.stores
        if (!customer.stores.contains(business)) addC2B(C2B(customer.mail, business))

        c2bListener?.remove()
        c2bListener = dbC2Bs.document(customerModel.customer?.mail + "-" + business).addSnapshotListener { snapshot, e ->
            e?.let { Log.w("StoreModel", "Listen failed.", e); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                selectedC2B.value = snapshot.toObject(C2B::class.java)
                Log.e("StoreModel", "Current data: " + selectedC2B.value)
            } else {
                Log.e("StoreModel", "Current data: null") //TODO Is this line never executed?
            }
        }
    }

    fun add2Cart(product: Product?) {
        Log.e("StoreModel", "add2Cart")
    }
}