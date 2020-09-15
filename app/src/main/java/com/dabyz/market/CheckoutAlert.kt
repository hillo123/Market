package com.dabyz.market

import android.app.AlertDialog
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.checkout_cart.view.*
import kotlinx.android.synthetic.main.checkout_cart_delivery.view.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast_validating.*


class CheckoutAlert(main: MainActivity) : AlertDialog(main) {
    private lateinit var phone: String
    private lateinit var mail: String
    private fun validateAndSave(dialogView: View): Boolean {
        phone = dialogView.etPhone.text.toString()
        mail = dialogView.etMail.text.toString()
        if (phone.isEmpty()) {
            //TODO completar validaciones de telefono
            Toast(context).apply {
                duration = Toast.LENGTH_LONG
                view = layoutInflater.inflate(R.layout.custom_toast_validating, clToastValidate)
                show()
            }
            return false
        }

        if (mail.isEmpty()) {
            //TODO completar validaciones de Mail
            Toast(context).apply {
                duration = Toast.LENGTH_LONG
                view = layoutInflater.inflate(R.layout.custom_toast_validating, clToastValidate)
                show()
            }
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
                    view.findViewById<TextView>(R.id.tvToastPositive).setText("Solicitud de preparación enviada")
                    show()
                }
                main.storeModel.addOrder(phone, mail)
                checkoutAlert.dismiss()
            }
        }
        dialogView.btnCheckoutDelivery.setOnClickListener {
            if (validateAndSave(dialogView)) {
                val titleOne = SpannableString("Confirmar pedido")
                titleOne.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, titleOne.length, 0)
                val deliveryView = LayoutInflater.from(main).inflate(R.layout.checkout_cart_delivery, null)
                Builder(main)
                    .setView(deliveryView)
                    .setTitle(titleOne)
                    .setNeutralButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Enviar") { dialog, _ ->
                        Toast(main).apply {
                            duration = Toast.LENGTH_LONG
                            view = layoutInflater.inflate(R.layout.custom_toast, clToast)
                            show()
                        }
                        //TODO validate Address
                        main.storeModel.addOrder(phone, mail, deliveryView.etAddress.text.toString())
                        dialog.dismiss()
                    }
                    .create()
                    .show()
                checkoutAlert.dismiss()
            }
        }
    }
}