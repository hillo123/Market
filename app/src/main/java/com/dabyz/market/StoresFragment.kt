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
import com.dabyz.market.models.Business
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_stores.*
import kotlinx.android.synthetic.main.stores_lines.view.*

class StoresFragment() : Fragment(R.layout.fragment_stores) {
    private val main by lazy { activity as MainActivity }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        main.storeModel.getStores()
        var storesAdapter = StoresListAdapter()
        recyclerViewStores.apply {
            layoutManager = LinearLayoutManager(main)
            adapter = storesAdapter
        }
        main.storeModel.allBusiness.observe(main as LifecycleOwner, Observer {
            storesAdapter.apply { this.businesses = it; notifyDataSetChanged() }
        })
    }

    inner class StoresListAdapter() : RecyclerView.Adapter<StoresListAdapter.ItemHolder>() {
        var businesses = listOf<Business>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresListAdapter.ItemHolder =
            ItemHolder(LayoutInflater.from(main).inflate(R.layout.stores_lines, parent, false))

        override fun getItemCount(): Int = businesses.size

        override fun onBindViewHolder(holder: StoresListAdapter.ItemHolder, position: Int) {
            holder.business = businesses[position]
            holder.itemView.etTitleStores.text = holder.business.name
        }

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var business: Business

            init {
                itemView.setOnClickListener {
                    main.storeModel.actualStore = business.mail
                    main.nav2Products()
                }
            }
        }
    }
}