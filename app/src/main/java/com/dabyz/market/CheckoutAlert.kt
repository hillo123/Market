package com.dabyz.market

import android.app.AlertDialog
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.checkout_cart.view.*
import kotlinx.android.synthetic.main.checkout_cart_delivery.view.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.custom_toast_validating.*

class CheckoutAlert(main: MainActivity) : AlertDialog(main) {
    private fun fancyToast(layout: Int) = Toast(context).apply {
        duration = Toast.LENGTH_LONG
        view = layoutInflater.inflate(layout, clToastValidate)
        show()
    }
    private lateinit var phone: String
    private lateinit var mail: String
    private fun validateAndSave(dialogView: View): Boolean {
        phone = dialogView.etPhone.text.toString()
        mail = dialogView.etMail.text.toString()
        if (phone.isEmpty()) {
            //TODO completar validaciones de telefono
            fancyToast(R.layout.custom_toast_validating)
            return false
        }
        if (mail.isEmpty()) {
            //TODO completar validaciones de Mail
            fancyToast(R.layout.custom_toast_validating)
            return false
        }
        return true
    }

    init {
        val title = SpannableString("Confirmar pedido")
        title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
        val dialogView = LayoutInflater.from(main).inflate(R.layout.checkout_cart, null)
        val checkoutAlert = Builder(main)
            .setView(dialogView)
            .setTitle(title)
            .setNeutralButton("Cancelar", null)
            .create()
        checkoutAlert.show()
        dialogView.btnCheckoutStore.setOnClickListener {
            if (validateAndSave(dialogView)) {
                Toast(context).apply {
                    duration = Toast.LENGTH_LONG
                    view = layoutInflater.inflate(R.layout.custom_toast, clToast)
                    view.tvToastPositive.text = "Solicitud de preparación enviada"
                    show()
                }
                main.storeModel.addOrder(phone, mail)
                checkoutAlert.dismiss()
            }
        }
        dialogView.btnCheckoutDelivery.setOnClickListener {
            if (validateAndSave(dialogView)) {
                val title = SpannableString("Confirmar pedido")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val deliveryView = LayoutInflater.from(main).inflate(R.layout.checkout_cart_delivery, null)
                Builder(main)
                    .setView(deliveryView)
                    .setTitle(title)
                    .setNeutralButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Enviar") { dialog, _ ->
                        Toast(main).apply {
                            duration = Toast.LENGTH_LONG
                            view = layoutInflater.inflate(R.layout.custom_toast, clToast)
                            show()
                        }
                        //TODO validate Address
                        main.storeModel.addOrder(phone, mail, deliveryView.etAddressCheckout.editText?.text.toString())
                        dialog.dismiss()
                    }
                    .create().show()
                checkoutAlert.dismiss()
            }
        }
    }
}