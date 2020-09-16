package com.dabyz.market

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dabyz.market.models.CustomerModel
import com.dabyz.market.models.StoreModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val storeModel by lazy { ViewModelProvider(this).get(StoreModel::class.java) }
    private val customerModel by lazy { ViewModelProvider(this).get(CustomerModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customerModel.init(this, storeModel)
        supportFragmentManager.beginTransaction().apply { replace(R.id.flFragment, ProductsFragment()); commit() }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            supportFragmentManager.beginTransaction().apply {
                supportFragmentManager.beginTransaction().apply {
                    when (it.itemId) {
                        R.id.miCart -> replace(R.id.flFragment, CartFragment())
                        R.id.miProducts -> replace(R.id.flFragment, ProductsFragment())
                        R.id.miStores -> replace(R.id.flFragment, StoresFragment())
                    }
                    commit()
                }
            }
            true
        }
    }
}