package com.c22ps305team.saferoute.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.data.ResultsItem
import com.c22ps305team.saferoute.databinding.ItemPlaceBinding

class ListPlaceAdapter(private val listPlace: List<ResultsItem>) :
    RecyclerView.Adapter<ListPlaceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaceAdapter.ViewHolder {
        TODO("Inflate the item view")
    }

    override fun onBindViewHolder(holder: ListPlaceAdapter.ViewHolder, position: Int) {
        TODO("Bind data to View")
    }

    override fun getItemCount(): Int = listPlace.size

    inner class ViewHolder(var binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)
}