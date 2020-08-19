package com.dabyz.market
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val main by lazy { activity as MainActivity }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main.txFragmentTitle.text = "Nuevo Usuario"
        btnSignUp.setOnClickListener {
            //TODO("Form validations and save in firebase")
            main.getSharedPreferences("dabyz.market", Context.MODE_PRIVATE).edit()
                ?.apply { putString("mail", etMail.text.toString()); commit() }
            /*
            main.customerModel.addBusiness(
                Customer(
                    etName.text.toString(), etMail.text.toString(), etPassword.text.toString(),
                    etPhone.text.toString(), etAddress.text.toString()
                )
            )
             */
            main.supportFragmentManager.beginTransaction()
                .apply { replace(R.id.flFragment, ProductsFragment()); commit() }
        }
        btnLogIn.setOnClickListener {
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }
}