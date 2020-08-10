package com.dabyz.market

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
  val storeModel by lazy { ViewModelProvider(this).get(StoreModel::class.java) }
  val customerModel by lazy { ViewModelProvider(this).get(CustomerModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportFragmentManager.beginTransaction().apply {
      val mail = getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).getString("mail", null)
      if (mail == null)
        replace(R.id.flFragment, SignUpFragment())
      else
        replace(R.id.flFragment, ProductsFragment(mail))
      commit()
    }
  }
}