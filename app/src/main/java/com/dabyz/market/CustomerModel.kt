package com.dabyz.market

import androidx.lifecycle.ViewModel


data class Customer(var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "")

class CustomerModel : ViewModel() {
    fun addBusiness(customer: Customer) {

    }

}
