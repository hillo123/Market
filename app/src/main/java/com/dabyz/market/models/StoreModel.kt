package com.dabyz.market.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class Product(var title: String = "", var title2: String = "", var price: Long = 0, var photo: String = "", var fileName: String = "")
data class Business(
    var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "",
    var refs: ArrayList<Product> = ArrayList()
)

class StoreModel : ViewModel() {
    private var businessListener: ListenerRegistration? = null
    private val dbBusiness = FirebaseFirestore.getInstance().collection("greengrocery")
    private var mail = "eamedina@gmail.com"
        set(value) {
            field = value; init()
        }

    val selectedBusiness = MutableLiveData<Business>()

    fun init() {
        businessListener?.remove()
        businessListener = dbBusiness.document(mail).addSnapshotListener { snapshot, e ->
            e?.let { Log.w("Model", "Listen failed.", e); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                selectedBusiness.value = snapshot.toObject(Business::class.java)
            } else {
                Log.d("Model", "Current data: null")
            }
        }
    }
}