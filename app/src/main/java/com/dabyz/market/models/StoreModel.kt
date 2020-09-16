package com.dabyz.market.models


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
data class Order(var address: String = "", var customer: String = "", val date: Date = Date(), var orderLines: ArrayList<Line> = ArrayList())
data class Cart(val customer: String = "", val business: String = "", var lines: ArrayList<Line> = ArrayList())

class StoreModel : ViewModel() {
    lateinit var customerModel: CustomerModel
    private var businessListener: ListenerRegistration? = null
    private val dbBusiness = FirebaseFirestore.getInstance().collection("greengrocery")
    private val dbC2Bs = FirebaseFirestore.getInstance().collection("c2bs")
    private val ordersC2B = FirebaseFirestore.getInstance().collection("orders")

    var actualStore: String? = null
        set(value) {
            field = value;
            value?.let {
                CoroutineScope(Main).launch {
                    selectedCart.value = getC2B()
                    initCurrentBusiness(value)
                }
            }
        }

    val selectedBusiness = MutableLiveData<Business>()
    val productQttys = MutableLiveData<List<Line>>()
    var selectedCart = MutableLiveData<Cart>()

    private fun updateProductQttys() {
        var pqs = selectedBusiness.value?.refs?.map { Line(it, 0) }
        enrichQuantitiesWithCart(pqs!!)
        productQttys.value = pqs
    }

    private fun enrichQuantitiesWithCart(productQttys: List<Line>) {
        selectedCart.value?.let {
            productQttys.forEach { l ->
                var cartLine = selectedCart.value?.lines!!.find { l.product == it.product }
                cartLine?.let { l.quantity = it.quantity }
            }
        }
    }

    private fun initCurrentBusiness(actualStore: String) {
        businessListener?.remove()
        businessListener = dbBusiness.document(actualStore).addSnapshotListener { snapshot, e ->
            e?.let { Log.w("Model", "Listen failed.", e); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                selectedBusiness.value = snapshot.toObject(Business::class.java)
                updateProductQttys()
            } else {
                Log.d("Model", "Current data: null")
            }
        }
    }

    private suspend fun getC2B() = withContext(IO) {
        suspendCoroutine<Cart?> { cont ->
            dbC2Bs.document(customerModel.customer.mail + "-" + actualStore).get()
                .addOnSuccessListener { cont.resume(it.toObject(Cart::class.java)) }.addOnFailureListener { cont.resume(null) }
        }
    }

    fun add2Cart(product: Product, q: Int) {
        var cart = selectedCart.value
        if (cart == null) {
            cart = Cart(customerModel.customer.mail, actualStore!!)
            customerModel.customer.stores.add(actualStore!!)
        }
        val line = cart.lines.find { it.product == product }
        if (line == null) cart.lines.add(Line(product, q))
        else {
            line.quantity += q
            if (line.quantity <= 0) cart.lines.remove(line)
        }
        dbC2Bs.document(cart.customer + "-" + cart.business).set(cart)
        selectedCart.value = cart
        updateProductQttys()
    }

    fun addOrder(phone: String, mail: String, address: String = "") {
        Log.e(null, phone + mail + address)
        // TODO update phone and mail in Customer and add New Customer Address
        customerModel.updateCustomer(phone, mail, address)
        // TODO save new Order
        //Toma el correo que esta registrado en el signUP
        ordersC2B.document(selectedCart.value?.customer.toString()).set(selectedCart.value!!)
        // TODO delete Cart
//        EN REVISIÓN
//        ordersC2B.document(selectedCart.value?.customer.toString()).delete()
    }

    fun deleteCart() {

    }
}