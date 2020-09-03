package com.dabyz.market

import android.os.Bundle
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
import com.dabyz.market.models.Product
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_product.view.*
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment() : Fragment(R.layout.fragment_products) {
    private val main by lazy { activity as MainActivity }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main.txFragmentTitle.text = "No hay tienda seleccionada"
        var productsAdapter = ProductsListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(main)
            adapter = productsAdapter
        }
        main.storeModel.selectedBusiness.observe(main as LifecycleOwner, Observer { business ->
            main.txFragmentTitle.text = business.name
            productsAdapter.apply {
                // TODO enrich business.refs with cart
                productQttys = business.refs.map { Line(it, 0) }
                notifyDataSetChanged()
            }
        })
    }

    inner class ProductsListAdapter() : RecyclerView.Adapter<ProductsListAdapter.ItemHolder>() {
        var productQttys = listOf<Line>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsListAdapter.ItemHolder =
            ItemHolder(LayoutInflater.from(main).inflate(R.layout.card_product, parent, false))

        override fun getItemCount(): Int = productQttys.size

        override fun onBindViewHolder(holder: ProductsListAdapter.ItemHolder, position: Int) =
            holder.setData(productQttys[position])

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

            fun setData(productQtty: Line) {
                this.productQtty = productQtty
                productQtty.product?.apply {
                    itemView.etTitle.text = title
                    itemView.etTitle2.text = title2
                    itemView.etPrice.text = price.toString()
                    Glide.with(main).load(photo).into(itemView.imgProduct)
                }
                itemView.tvQtty.text = productQtty.quantity.toString()
            }
        }
    }
}