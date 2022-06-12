package com.c22ps305team.saferoute.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c22ps305team.saferoute.data.InfoTips
import com.c22ps305team.saferoute.databinding.ItemTipsBinding

class TipsInfoAdapter(private val listTips: ArrayList<InfoTips>) :
    RecyclerView.Adapter<TipsInfoAdapter.TipsViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bind(listTips[position])
    }

    override fun getItemCount(): Int {
        return listTips.size
    }


    inner class TipsViewHolder(var binding: ItemTipsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(infoTips: InfoTips){

            binding.apply {
                Glide.with(itemView).load(infoTips.photo).into(imgStories)
                tvName.text = infoTips.tittle

                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(infoTips)
                }
            }

        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(infoTips: InfoTips)
    }

}