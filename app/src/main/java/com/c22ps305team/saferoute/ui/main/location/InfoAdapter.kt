package com.c22ps305team.saferoute.ui.main.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.databinding.ItemAreaInfoBinding

class InfoAdapter(private val crimeInfo: MutableMap<String, Int>): RecyclerView.Adapter<InfoAdapter.ViewHolder>() {

    //Convert Map to List
    private val dataKey: List<String> = crimeInfo.toList().map { it.first }
    private val dataValue: List<String> = crimeInfo.toList().map { "${it.second}"}


    inner class ViewHolder(val binding: ItemAreaInfoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(key: String, value: String){
            binding.apply {
                tvAreaName.text = key
                tvStatePercentage.text = value
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAreaInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataKey[position], dataValue[position])
    }

    override fun getItemCount(): Int = crimeInfo.size

}
