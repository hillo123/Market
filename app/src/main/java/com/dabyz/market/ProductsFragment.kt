package com.dabyz.market

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class ProductsFragment(private val mail: String) : Fragment(R.layout.fragment_products) {
    private val main by lazy { activity as MainActivity }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main.txFragmentTitle.text = "Lista de Productos"
        /*
        main.storeModel.init(mail)
        var productsAdapter = ProductsListAdapter(main, main.storeModel)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(main)
            adapter = productsAdapter
        }
        main.storeModel.selectedBusiness.observe(main as LifecycleOwner, Observer {
            productsAdapter.apply { products = it.refs; notifyDataSetChanged() }
        })
        btnOrders.setOnClickListener {
            Toast.makeText(context, "Orders not implemented yet", Toast.LENGTH_SHORT).show()
        }
        btnNewProduct.setOnClickListener {
            main.supportFragmentManager?.beginTransaction()
                ?.apply {
                    replace(R.id.flFragment, NewProductFragment())
                    addToBackStack(null)
                    commit()
                }
        */
    }
/*
    class ProductsListAdapter(val main: MainActivity, val storeModel: StoreModel) : RecyclerView.Adapter<ProductsListAdapter.ItemHolder>() {
        var products = listOf<Product>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsListAdapter.ItemHolder =
            ItemHolder(LayoutInflater.from(main).inflate(R.layout.card_product, parent, false))

        override fun getItemCount(): Int = products.size

        override fun onBindViewHolder(holder: ProductsListAdapter.ItemHolder, position: Int) =
            holder.setData(products[position], position)

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var product: Product? = null

            init {
                itemView.setOnClickListener {
                    main.supportFragmentManager.beginTransaction()
                        ?.apply {
                            replace(R.id.flFragment, EditProductFragment(product!!))
                            addToBackStack(null); commit()
                        }
                }
            }

            fun setData(product: Product?, pos: Int) {
                this.product = product as Product
                product.apply {
                    itemView.etTitle.text = title
                    itemView.etTitle2.text = title2
                    itemView.etPrice.text = price.toString()
                    Glide.with(main).load(photo).into(itemView.imgProduct)
                }

            }
        }
    }

 */
}