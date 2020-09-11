package com.dabyz.market

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dabyz.market.models.Line
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_product.view.btnAdd2Cart
import kotlinx.android.synthetic.main.card_product.view.btnRemove2Cart
import kotlinx.android.synthetic.main.card_product.view.etPrice
import kotlinx.android.synthetic.main.card_product.view.etTitle
import kotlinx.android.synthetic.main.card_product.view.etTitle2
import kotlinx.android.synthetic.main.card_product.view.imgProduct
import kotlinx.android.synthetic.main.card_product.view.tvQtty
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(R.layout.fragment_cart) {
    private val main by lazy { activity as MainActivity }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main.txFragmentTitle.text = "No hay tienda seleccionada"
        var cartAdapter = CartAdapter()
        rvCart.apply {
            layoutManager = LinearLayoutManager(main)
            adapter = cartAdapter
        }
        main.storeModel.selectedBusiness.observe(main as LifecycleOwner, Observer { business ->
            main.txFragmentTitle.text = business.name
        })
        main.storeModel.selectedCart.observe(main as LifecycleOwner, Observer {
            cartAdapter.notifyDataSetChanged()
        })
    }

    inner class CartAdapter() : RecyclerView.Adapter<CartAdapter.ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ItemHolder =
            ItemHolder(LayoutInflater.from(main).inflate(R.layout.cart_line, parent, false))

        override fun getItemCount(): Int =
            if (main.storeModel.selectedCart.value == null) 0 else main.storeModel.selectedCart.value?.lines!!.size

        override fun onBindViewHolder(holder: CartAdapter.ItemHolder, position: Int) =
            holder.setData(main.storeModel.selectedCart.value?.lines!![position])

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var productQtty: Line

            init {
                btnCheckout.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    val title = SpannableString("Confirmar pedido")
                    title.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        title.length,
                        0
                    )
                    builder.setTitle(title)
                    builder.setMessage("Deseas confirmar el pedido.")
                    builder.setPositiveButton("SI", null)
                    builder.setNegativeButton("No", null)
                    // Dejar solo dos botones o tres botones?
                    builder.setNeutralButton("Cancelar", null)
                    val dialog = builder.create()
                    dialog.show()
                }
                itemView.btnRemove2Cart.setOnClickListener {
                    if (productQtty.quantity >= 1) main.storeModel.add2Cart(productQtty.product!!, -1)
                }
                itemView.btnAdd2Cart.setOnClickListener {
                    main.storeModel.add2Cart(productQtty.product!!, 1)
                }
            }

            fun setData(cartLine: Line) {
                this.productQtty = cartLine
                cartLine.product?.apply {
                    itemView.etTitle.text = title
                    itemView.etTitle2.text = " /$title2"
                    itemView.etPrice.text = price.toString()
                    Glide.with(main).load(photo).into(itemView.imgProduct)
                }
                itemView.tvQtty.text = cartLine.quantity.toString()
                hideShowBtnRemove2Cart()
            }
            val checkout = itemView.btnRemove2Cart
            private fun hideShowBtnRemove2Cart() {
                if (productQtty.quantity.toString() > "0") {
                    checkout.visibility = View.VISIBLE;
                } else {
                    checkout.visibility = View.GONE;
                }
            }
        }
    }
}