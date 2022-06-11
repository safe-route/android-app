package com.c22ps305team.saferoute.repository

import com.c22ps305team.saferoute.api.ApiService
import com.c22ps305team.saferoute.data.auth.AuthRequest
import com.c22ps305team.saferoute.database.UserPreferences


class UserAuthRepository(
    private val api: ApiService,
    private val userPref: UserPreferences
) {


    fun register(username: String, password: String) = api.register(username, password)

    suspend fun login(username: String, password: String) = api.login(username, password)

    suspend fun saveToken(token: String) = userPref.saveToken(token)

    suspend fun saveUser(user: String) = userPref.saveUser(user)


    fun fetchToken() = userPref.fetchToken()

    fun fetchUser() = userPref.fetchUser()


    suspend fun deleteUser() = userPref.deleteUser()




    companion object {
        private var INSTANCE: UserAuthRepository? = null
        fun getInstance(api: ApiService, userPref: UserPreferences): UserAuthRepository {
            return INSTANCE ?: synchronized(this) {
                UserAuthRepository(api, userPref).also {
                    INSTANCE = it
                }
            }
        }

    }






}