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

data class Line (val product:Product, var quantity:Int)
data class Order (val date: Date = Date(), var orderLines: ArrayList<Line> = ArrayList())
data class C2B(val business: String, val customer: String, var orders: ArrayList<Order> = ArrayList())

class StoreModel : ViewModel() {
    private var businessListener: ListenerRegistration? = null
    private val dbBusiness = FirebaseFirestore.getInstance().collection("greengrocery")
    var actualStore = ""
        set(value) {
            field = value;
            initCurrentBusiness()
        }

    val selectedBusiness = MutableLiveData<Business>()
    private fun initCurrentBusiness() {
        businessListener?.remove()
        businessListener = dbBusiness.document(actualStore).addSnapshotListener { snapshot, e ->
            e?.let { Log.w("Model", "Listen failed.", e); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                selectedBusiness.value = snapshot.toObject(Business::class.java)
            } else {
                Log.d("Model", "Current data: null")
            }
        }
    }
}