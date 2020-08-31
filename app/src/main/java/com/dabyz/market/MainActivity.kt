package com.dabyz.market

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dabyz.market.models.CustomerModel
import com.dabyz.market.models.StoreModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val storeModel by lazy { ViewModelProvider(this).get(StoreModel::class.java) }
    val customerModel by lazy { ViewModelProvider(this).get(CustomerModel::class.java) }

    private fun initModels() {
        customerModel.storeModel = storeModel
        customerModel.mail = getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("customerMail", null)
        storeModel.mail = "eamedina@gmail.com"
    }

    fun savePreferences(mail: String) =
        getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).edit()?.apply { putString("customerMail", mail); commit() }

    private val fragments = hashMapOf(
        R.id.miCart to CartFragment(), R.id.miProducts to ProductsFragment(), R.id.miStores to StoresFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initModels()
        supportFragmentManager.beginTransaction().apply {
            if (customerModel.mail == null)
                replace(R.id.flFragment, SignUpFragment())
            else
                replace(R.id.flFragment, ProductsFragment())
            commit()
        }
        bottomNavigationView.setOnNavigationItemSelectedListener {
            supportFragmentManager.beginTransaction().apply { replace(R.id.flFragment, fragments[it.itemId] as Fragment); commit() }
            true
        }
    }
}