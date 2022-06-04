package com.c22ps305team.saferoute.ui.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.data.Statistic
import com.c22ps305team.saferoute.databinding.ActivityDetailPlaceBinding

class DetailPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val data = intent.getParcelableExtra<Statistic>(EXTRA_STATS) as Statistic
        val dataCrime: MutableMap<String, Int> = data.crime_info!!

        /*val dataKey: List<String> = data.crime_info!!.toList().map { it.first}
        val dataValue: List<String> = data.crime_info!!.toList().map { "${it.second}"}*/


        binding.tesData.text = data.subdistrict.toString()
        binding.tesData2.text = data.crime_info.toString()

        //binding.tesData3.text = dataList.toString()

        val adapterList = CrimeInfoAdapter(dataCrime)
        binding.rvArea.apply {
            adapter = adapterList
            layoutManager = LinearLayoutManager(this@DetailPlaceActivity, LinearLayoutManager.VERTICAL, false)

        }
    }


    companion object {
        const val EXTRA_STATS = "extra_stats"
    }

}