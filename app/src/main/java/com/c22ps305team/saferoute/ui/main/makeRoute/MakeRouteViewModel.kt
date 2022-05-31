package com.c22ps305team.saferoute.ui.main.makeRoute

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c22ps305team.saferoute.api.ApiDirectionMapsConfig
import com.c22ps305team.saferoute.data.DirectionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeRouteViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _geocodeWaypoint = MutableLiveData<DirectionsResponse>()
    val gecodeWaypoint: LiveData<DirectionsResponse> = _geocodeWaypoint

    fun getDirection(direction: String, destination: String, apiKey: String) {
        _isLoading.value = true

        val client =
            ApiDirectionMapsConfig.getApiService().getDirection(direction, destination, apiKey)
        client.enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _geocodeWaypoint.value = response.body()
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                Log.e("Error Gan: ", "onFailure: ${t.message}")
            }

        })

    }


}