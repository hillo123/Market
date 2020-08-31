package com.dabyz.market
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dabyz.market.models.Customer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val main by lazy { activity as MainActivity }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main.bottomNavigationView.visibility = INVISIBLE
        main.txFragmentTitle.text = "Nuevo Usuario"
        btnSignUp.setOnClickListener {
            //TODO("Form validations and save in firebase")
            main.customerModel.addCustomer(
                Customer(
                    etName.text.toString(), etMail.text.toString(), etPassword.text.toString(),
                    etPhone.text.toString(), etAddress.text.toString()
                )
            )
            main.bottomNavigationView.visibility = VISIBLE
            main.supportFragmentManager.beginTransaction()
                .apply { replace(R.id.flFragment, ProductsFragment()); commit() }
        }
        btnLogIn.setOnClickListener {
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }
}