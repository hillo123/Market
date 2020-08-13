package com.dabyz.market

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class MainActivity : AppCompatActivity() {

    private val mail: String = "eamedina@gmail.com"
    val fragments = hashMapOf(
        R.id.miCart to CartFragment(),
        R.id.miProducts to ProductsFragment(mail),
        R.id.miStores to StoresFragment()
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().apply {
            val mail =
                getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("mail", null)
            if (mail == null)
                replace(R.id.flFragment, SignUpFragment())
            else
                replace(R.id.flFragment, ProductsFragment(mail))
            commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miCart -> nav(CartFragment())
                R.id.miProducts -> nav(ProductsFragment(mail))
                R.id.miStores -> nav(StoresFragment())
            }
            true
        }
    }

    private fun nav(fragment: Fragment) = supportFragmentManager.beginTransaction()
        .apply { replace(R.id.flFragment, fragment); commit() }

}