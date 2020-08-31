package com.dabyz.market.models

import androidx.lifecycle.ViewModel


data class Customer(var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "")

class CustomerModel : ViewModel() {
    lateinit var storeModel: StoreModel
    var mail: String? = null

    fun addCustomer(customer: Customer) {
        //TODO save customer to firebase
        storeModel.actualStore = "eamedina@gmail.com"
    }

}
