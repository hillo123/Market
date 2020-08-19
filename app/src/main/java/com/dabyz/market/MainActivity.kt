package com.dabyz.market

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dabyz.market.models.StoreModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val storeModel by lazy { ViewModelProvider(this).get(StoreModel::class.java) }
    private val fragments = hashMapOf(
        R.id.miCart to CartFragment(), R.id.miProducts to ProductsFragment(), R.id.miStores to StoresFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().apply {
            val mail = getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("mail", null)
            if (mail == null)
                replace(R.id.flFragment, SignUpFragment())
            else
                replace(R.id.flFragment, ProductsFragment())
            commit()
        }
        bottomNavigationView.setOnNavigationItemSelectedListener {
            supportFragmentManager.beginTransaction()
                .apply { replace(R.id.flFragment, fragments[it.itemId] as Fragment); commit() }
            true
        }
    }
}