package com.c22ps305team.saferoute.ui.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.c22ps305team.saferoute.data.InfoTips
import com.c22ps305team.saferoute.databinding.ActivityDetailTipsBinding

class DetailTipsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTipsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupContent()
    }

    private fun setupContent() {
        val data = intent.getParcelableExtra<InfoTips>(EXTRA_TIPS) as InfoTips
        binding.apply {
            Glide.with(this@DetailTipsActivity).load(data.photo).into(ivTips)
            tvTittle.text = data.tittle
            tvContent.text = data.content
            tvSource.text = data.source
        }
    }


    companion object {
        const val EXTRA_TIPS = "extra_tips"
    }
}