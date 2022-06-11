package com.c22ps305team.saferoute.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.c22ps305team.saferoute.data.auth.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {


    suspend fun saveToken(token: String){
        dataStore.edit { preference ->
            preference[TOKEN_KEY] = token
        }
    }

    suspend fun saveUser(user: String){
        dataStore.edit { preference ->
            preference[USER] = user
        }
    }



    fun fetchToken(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[TOKEN_KEY] ?: ""
        }
    }


    fun fetchUser(): Flow<String> {
        return dataStore.data.map { preference ->
            preference[USER] ?: ""
        }
    }

    suspend fun deleteUser(){
        dataStore.edit { preference ->
            preference.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER = stringPreferencesKey("user")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }


    }



}