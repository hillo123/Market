package com.dabyz.market

import android.app.AlertDialog
import android.content.Context
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.dabyz.market.models.Order
import kotlinx.android.synthetic.main.checkout_cart.view.*
import kotlinx.android.synthetic.main.checkout_cart_delivery.view.*

class CheckoutAlert(main: MainActivity) : AlertDialog(main) {
    private lateinit var phone: String
    private lateinit var mail: String

    private fun validateAndSave(dialogView: View): Boolean {
        phone = dialogView.etPhone.text.toString()
        mail = dialogView.etMail.text.toString()
        if (phone.isEmpty()) { //TODO completar validaciones de telefono
            Toast.makeText(context, "Debe indicar telefono y correo validos", Toast.LENGTH_LONG).show()
            return false
        }

        if (mail.isEmpty()) { //TODO completar validaciones de Mail
            Toast.makeText(context, "Debe indicar telefono y correo validos", Toast.LENGTH_LONG).show()
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
                Toast.makeText(main, "Solicitud de preparación enviada", Toast.LENGTH_LONG).show()
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
                        Toast.makeText(main, "Solicitud de pedido a domicilio enviada", Toast.LENGTH_LONG).show()
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