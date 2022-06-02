package com.c22ps305team.saferoute.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.data.ResultsItem
import com.c22ps305team.saferoute.databinding.ItemPlaceBinding

class ListPlaceAdapter(private val listPlace: List<ResultsItem>) :
    RecyclerView.Adapter<ListPlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaceAdapter.ViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListPlaceAdapter.ViewHolder, position: Int) {
        holder.binding.tvPlaceName.text = listPlace[position].name
        holder.binding.tvPlaceDetail.text = listPlace[position].formattedAddress
    }

    override fun getItemCount(): Int = listPlace.size

    inner class ViewHolder(var binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)
}