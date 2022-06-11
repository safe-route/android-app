package com.c22ps305team.saferoute.api

import com.c22ps305team.saferoute.data.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String,
    ): Call<DirectionsResponse>

    @GET("place/textsearch/json")
    fun searchPlace(
        @Query("location") location: String,
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("key") key: String
    ): Call<SearchPlaceResponse>


    //Report
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("add-row-jakarta_crime_history")
    suspend fun reportCrime(
        @Body crimeReport: JsonObject
    ): Response<ReportCrimeResponse>

    // Coordinate
    @GET("/")
    fun getAllCoordinate(): Call<CoordinateResponse>

    // Area Statistic
    @POST("/")
    fun getAreaStatistic(
        @Body areaName: JsonObject
    ): Call<AreaStatisticResponse>


}