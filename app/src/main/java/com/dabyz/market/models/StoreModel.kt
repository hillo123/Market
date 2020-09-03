package com.dabyz.market.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class Product(var title: String = "", var title2: String = "", var price: Long = 0, var photo: String = "", var fileName: String = "")
data class Business(
    var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "",
    var refs: ArrayList<Product> = ArrayList()
)

data class Line(var product: Product? = null, var quantity: Int = 0)
data class Order(val date: Date = Date(), var orderLines: ArrayList<Line> = ArrayList())
data class C2B(val customer: String = "", val business: String = "", var orders: ArrayList<Order> = ArrayList(), var cart: ArrayList<Line> = ArrayList())

class StoreModel : ViewModel() {
    lateinit var customerModel: CustomerModel
    private var businessListener: ListenerRegistration? = null
    private val dbBusiness = FirebaseFirestore.getInstance().collection("greengrocery")
    private val dbC2Bs = FirebaseFirestore.getInstance().collection("c2bs")
    private var selectedC2B: C2B? = null

    var actualStore: String? = null
        set(value) {
            field = value;
            value?.let { initCurrentBusiness(value) }
        }

    val selectedBusiness = MutableLiveData<Business>()
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
    }

    private suspend fun addC2B(c2b: C2B) = suspendCoroutine<Unit> { cont ->
        dbC2Bs.document(c2b.customer + "-" + c2b.business).set(c2b)
            .addOnSuccessListener { cont.resume(Unit) }.addOnFailureListener { cont.resume(Unit) }
    }

    private suspend fun getC2B() = suspendCoroutine<C2B?> { cont ->
        dbC2Bs.document(customerModel.customer?.mail + "-" + actualStore).get()
            .addOnSuccessListener { cont.resume(it.toObject(C2B::class.java)) }.addOnFailureListener { cont.resume(null) }
    }

    fun add2Cart(product: Product, q: Int) = CoroutineScope(IO).launch {
        if (selectedC2B == null) selectedC2B = getC2B()
        if (selectedC2B == null) {
            selectedC2B = C2B(customerModel.customer.mail, actualStore!!)
            customerModel.customer.stores.add(actualStore!!)
        }
        val line = selectedC2B?.cart?.find { it.product == product }
        if (line == null) selectedC2B?.cart?.add(Line(product, q)) else line.quantity += q
        addC2B(selectedC2B!!)
    }
}