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
import com.dabyz.market.models.LineStores
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
            storesAdapter.apply { this.business = it; notifyDataSetChanged() }
        })
    }

    inner class StoresListAdapter() : RecyclerView.Adapter<StoresListAdapter.ItemHolder>() {
        var business = listOf<Business>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresListAdapter.ItemHolder =
            ItemHolder(LayoutInflater.from(main).inflate(R.layout.stores_lines, parent, false))

        override fun getItemCount(): Int = business.size

        override fun onBindViewHolder(holder: StoresListAdapter.ItemHolder, position: Int) =
            holder.setData(business[position])

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            init {

            }

            fun setData(business: Business) {
                itemView.etTitleStores.text = business.name
                //TODO show address
            }
        }
    }
}