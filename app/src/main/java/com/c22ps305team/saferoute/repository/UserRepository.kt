package com.c22ps305team.saferoute.repository

import com.c22ps305team.saferoute.api.ApiService
import com.google.gson.JsonObject

class UserRepository(private val apiService: ApiService) {

    suspend fun uploadCrimeReport(report: JsonObject) =
        apiService.reportCrime(report)


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