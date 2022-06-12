package com.c22ps305team.saferoute.ui.main.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c22ps305team.saferoute.api.ApiAreaStatisticConfig
import com.c22ps305team.saferoute.data.AreaStatisticResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dataAreaStatistic = MutableLiveData<AreaStatisticResponse>()
    val dataAreaStatistic: LiveData<AreaStatisticResponse> = _dataAreaStatistic


    fun getDataAreaStatistic(areaName: JsonObject) {
        _isLoading.value = true
        val client = ApiAreaStatisticConfig.getApiService().getAreaStatistic(areaName)
        client.enqueue(object : Callback<AreaStatisticResponse> {
            override fun onResponse(
                call: Call<AreaStatisticResponse>,
                response: Response<AreaStatisticResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _dataAreaStatistic.value = response.body()
                }
            }

            override fun onFailure(call: Call<AreaStatisticResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("Error Gan: ", "onFailure: ${t.message}")
            }

        })
    }

}