package com.c22ps305team.saferoute.ui.main.makeRoute

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c22ps305team.saferoute.api.ApiMapsConfig
import com.c22ps305team.saferoute.data.DirectionsResponse
import com.c22ps305team.saferoute.data.ResultsItem
import com.c22ps305team.saferoute.data.SearchPlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeRouteViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _geocodeWaypoint = MutableLiveData<DirectionsResponse>()
    val gecodeWaypoint: LiveData<DirectionsResponse> = _geocodeWaypoint

    private val _resultPlace = MutableLiveData<SearchPlaceResponse>()
    val resultPlace: LiveData<SearchPlaceResponse> = _resultPlace

    private val _listPlace = MutableLiveData<List<ResultsItem?>>()
    val listPlace: LiveData<List<ResultsItem?>> = _listPlace

    // Get Direction
    fun getDirection(origin: String, destination: String, apiKey: String) {
        _isLoading.value = true

        val client =
            ApiMapsConfig.getApiService().getDirection(origin, destination, apiKey)
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

    // Search Place
    fun searchPlace(query: String, apiKey: String) {
        _isLoading.value = true

        val client =
            ApiMapsConfig.getApiService().searchPlace("-6.229728,106.6894283", query, "id", apiKey)
        client.enqueue(object : Callback<SearchPlaceResponse> {
            override fun onResponse(
                call: Call<SearchPlaceResponse>,
                response: Response<SearchPlaceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listPlace.value = response.body()?.results!!
                }
            }

            override fun onFailure(call: Call<SearchPlaceResponse>, t: Throwable) {
                Log.e("Error Gan: ", "onFailure: ${t.message}")
            }

        })
    }


}