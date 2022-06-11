package com.c22ps305team.saferoute.ui.main.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.c22ps305team.saferoute.data.Statistic
import com.c22ps305team.saferoute.databinding.ActivityDetailPlaceBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class DetailPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPlaceBinding
    private lateinit var barChart: BarChart

    var dataKey: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barChart = binding.barChart

        initBarChart()
        showBarChart()
    }


    private fun showBarChart() {
        val data = intent.getParcelableExtra<Statistic>(EXTRA_STATS) as Statistic
        val dataCrime: MutableMap<String, Int> = data.crime_info!!
        //Log.e( "data", dataCrime.toString())

        //sort data
        val sortedData = dataCrime.toSortedMap(compareBy{it})
        //Log.e( "sorted data", sortedData.toString())

        //val dataKey: List<String> = data.crime_info!!.toList().map { it.first}
        dataKey = data.crime_info!!.toList().map { it.first}


        val dataValue: List<String> = sortedData.toList().map { "${it.second}"}
        //Log.e("dataValue ", dataValue.toString())


        binding.titleBar.text = data.subdistrict.toString()


        val adapterList = CrimeInfoAdapter(sortedData)
        binding.rvArea.apply {
            adapter = adapterList
            layoutManager = LinearLayoutManager(this@DetailPlaceActivity, LinearLayoutManager.VERTICAL, false)

        }


        //val valueList = ArrayList<Double>()
        val entries = ArrayList<BarEntry>()
        val title = "Title"

        //input data
        /*for (i in 1..22) {
            valueList.add(i * 100.1)
        }*/


        //fit the data into a bar
        /*for (i in valueList.indices) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }*/

        for (i in dataValue.indices) {
            val barEntry = BarEntry(i.toFloat(), dataValue[i].toFloat())
            entries.add(barEntry)
        }


        val barDataSet = BarDataSet(entries, title)
        val dataSet = BarData(barDataSet)
        barChart.data = dataSet
        barChart.invalidate()

        initBarDataSet(barDataSet)
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {
        //Changing the color of the bar
        barDataSet.color = Color.parseColor("#34cfeb") //#304567
        //Setting the size of the form in the legend
        barDataSet.formSize = 15f
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f
    }

    private fun initBarChart() {
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false)
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false)

        //remove the description label text located at the lower right corner
        val description = Description()
        description.isEnabled = false
        barChart.description = description

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(2000)
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000)

        val xAxis = barChart.xAxis
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)

        val leftAxis = barChart.axisLeft
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false)

        val rightAxis = barChart.axisRight
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false)

        barChart.legend.isEnabled = false
    }


    companion object {
        const val EXTRA_STATS = "extra_stats"
    }

}