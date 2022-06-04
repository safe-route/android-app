package com.c22ps305team.saferoute.ui.main.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.databinding.ItemCrimeInfoBinding

class CrimeInfoAdapter(private val crimeInfoList: MutableMap<String, Int>) : RecyclerView.Adapter<CrimeInfoAdapter.ViewHolder>() {

    //Convert Map to List
    private val dataKey: List<String> = crimeInfoList.toList().map { it.first }
    private val dataValue: List<String> = crimeInfoList.toList().map { "${it.second}"}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCrimeInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataKey[position], dataValue[position])
    }

    override fun getItemCount() = crimeInfoList.size

    inner class ViewHolder(var binding: ItemCrimeInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(key: String, value: String){
            binding.apply {
                tvAreaName.text = key
                tvStatePercentage.text = value
                //Log.e("bind ", crimeInfo )
            }
        }
    }

}

















