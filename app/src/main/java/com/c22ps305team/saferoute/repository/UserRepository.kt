package com.c22ps305team.saferoute.repository

import com.c22ps305team.saferoute.api.ApiService

class UserRepository(private val apiService: ApiService) {


    suspend fun uploadCrimeReport(date: String, time: Int, lat: Float, long: Float, typeCrime: String) =
        apiService.reportCrime(date, time, lat, long, typeCrime)



    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService): UserRepository {
            return INSTANCE ?: synchronized(this){
                UserRepository(apiService).also {
                    INSTANCE = it
                }
            }
        }
    }

}