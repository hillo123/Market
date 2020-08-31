package com.dabyz.market.models

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel


data class Customer(var name: String = "", val mail: String = "", var password: String = "", var phone: String = "", var address: String = "", var actualStore: String = "eamedina@gmail.com")

class CustomerModel : ViewModel() {
    lateinit var storeModel: StoreModel
    lateinit var ctx: Context
    var mail: String? = null

    fun addCustomer(customer: Customer) {
        ctx.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).edit().apply { putString("customerMail", customer.mail); commit() }
        //TODO save customer to firebase
        storeModel.actualStore = customer.actualStore
    }

    fun init(ctx: Activity, storeModel: StoreModel) {
        this.ctx = ctx
        this.storeModel = storeModel
        mail = ctx.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("customerMail", null)
        storeModel.actualStore = "eamedina@gmail.com"
    }
}