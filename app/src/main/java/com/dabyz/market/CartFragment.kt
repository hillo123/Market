package com.dabyz.market

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dabyz.market.models.Line
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_product.view.*
import kotlinx.android.synthetic.main.checkout_cart.view.*
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

        btnCheckout.setOnClickListener {
            CheckoutAlert(main)
        }
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
                itemView.btnRemove2Cart.setOnClickListener {
                    main.storeModel.add2Cart(productQtty.product!!, -1)
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
                itemView.btnRemove2Cart.visibility =
                    if (productQtty.quantity.toString() > "0") View.VISIBLE else View.GONE
            }
        }
    }
}