package com.dabyz.market

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class Product(var title: String = "", var title2: String = "", var price: Long = 0, var photo: String = "", var fileName: String = "")
data class Business(
    var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "",
    var refs: ArrayList<Product> = ArrayList()
)

class StoreModel : ViewModel() {
    private val dbBusiness = FirebaseFirestore.getInstance().collection("greengrocery")
    private val fbStorage by lazy { FirebaseStorage.getInstance().reference }
    private var mail = "" // TODO: load from local storage
    val selectedBusiness = MutableLiveData<Business>()

    fun init(mail: String) {
        this.mail = mail
        dbBusiness.document(mail).addSnapshotListener { snapshot, e ->
            e?.let { Log.w("Model", "Listen failed.", e); return@addSnapshotListener }
            if (snapshot != null && snapshot.exists()) {
                selectedBusiness.value = snapshot.toObject(Business::class.java)
            } else {
                Log.d("Model", "Current data: null")
            }
        }
    }

    fun addProduct(product: Product, compressImg: ByteArray) = CoroutineScope(IO).launch {
        product.fileName = "" + System.currentTimeMillis()
        product.photo = savePhoto(compressImg, product.fileName)
        dbBusiness.document(mail).update("refs", FieldValue.arrayUnion(product))
    }

    fun updateProduct(product: Product, compressImg: ByteArray?, oldProduct: Product) = CoroutineScope(IO).launch {
        if (compressImg != null) {
            product.fileName = "" + System.currentTimeMillis()
            product.photo = savePhoto(compressImg, product.fileName)
            fbStorage.child(mail + "/" + oldProduct.fileName + ".JPEG").delete()
                .addOnFailureListener { Log.e("StoreModel", "file " + mail + "/" + product.fileName + ".JPEG deleted error", it) }
        } else {
            product.fileName = oldProduct.fileName
            product.photo = oldProduct.photo
        }
        dbBusiness.document(mail).update("refs", FieldValue.arrayUnion(product))
        dbBusiness.document(mail).update("refs", FieldValue.arrayRemove(oldProduct))
            .addOnFailureListener { Log.e("StoreModel", "$oldProduct deleted error", it) }
    }

    private suspend fun savePhoto(compressImg: ByteArray, fileName: String): String =
        suspendCoroutine { cont ->
            val ref = fbStorage.child("$mail/$fileName.JPEG")
            ref.putBytes(compressImg).addOnCompleteListener {
                ref.downloadUrl.addOnCompleteListener() { cont.resume(it.result.toString()) }
            }
        }

    fun removeProduct(product: Product) {
        dbBusiness.document(mail).update("refs", FieldValue.arrayRemove(product))
        fbStorage.child(mail + "/" + product.fileName + ".JPEG").delete()
            .addOnFailureListener { Log.e("StoreModel", "file " + mail + "/" + product.fileName + ".JPEG deleted ok", it) }
    }

    fun saveBusiness(business: Business) {
        // TODO: 21/06/2020 save a new business to fire-storage and local storage
        //dbBusiness.document(selectedKey).
    }

    fun addFruteria() {
        val fruteria = hashMapOf(
            "Id" to 2,
            "address" to "https://www.google.com.ar/maps/place/Los+Chicos/@40.4335005,-3.6740017,20.25z/data=!4m8!1m2!2m1!1sfruteria!3m4!1s0x0:0x5f7f04c62279cc82!8m2!3d40.4335821!4d-3.6740694",
            "refs" to listOf(
                hashMapOf(
                    "photo" to "https://prod-mercadona.imgix.net/images/53687de52a3b197dc1d7c3417d693ef8.jpg?fit=crop&h=300&w=300",
                    "price" to 33,
                    "title" to "Manzana golden2",
                    "title2" to "Pieza 210 g aprox."
                )
            )
        )
        FirebaseFirestore.getInstance().collection("fruterias")
            .add(fruteria)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Model.addFruteria",
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("Model.addFruteria", "Error adding document", e)
            }
    }

    fun addBusiness(business: Business) = runBlocking {
        dbBusiness.document(business.mail).set(business)
    }
}