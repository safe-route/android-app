package com.c22ps305team.saferoute.api

import com.c22ps305team.saferoute.data.DirectionsResponse
import com.c22ps305team.saferoute.data.SearchPlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String,
    ): Call<DirectionsResponse>

    @GET("place/textsearch/json")
    fun searchPlace(
        @Query("query") query: String,
        @Query("key") key: String
    ): Call<SearchPlaceResponse>

}