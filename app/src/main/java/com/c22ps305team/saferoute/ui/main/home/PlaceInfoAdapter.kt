package com.c22ps305team.saferoute.ui.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.data.Statistic
import com.c22ps305team.saferoute.databinding.AreaInfoBinding

class PlaceInfoAdapter(private val listPlaceInfo: List<Statistic>) :
    RecyclerView.Adapter<PlaceInfoAdapter.PlaceInfoViewHolder>() {


    private lateinit var onItemClickCallback: OnItemClickCallback


    /*fun setData(list: List<Statistic>){
        listPlaceInfo.clear()
        listPlaceInfo.addAll(list)
        notifyDataSetChanged()
    }*/

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceInfoViewHolder {
        val binding = AreaInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceInfoViewHolder, position: Int) {
        holder.bind(listPlaceInfo[position])
    }

    override fun getItemCount() = listPlaceInfo.size


    inner class PlaceInfoViewHolder(var binding: AreaInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStatistic: Statistic){
            binding.apply {
                tvAreaName.text = listStatistic.subdistrict
                tvStatePercentage.text = listStatistic.totalCrime.toString()
                Log.e("list data", listStatistic.toString())
            }
            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(listStatistic)
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(statistic: Statistic)
    }



}