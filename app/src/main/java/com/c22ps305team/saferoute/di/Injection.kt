package com.c22ps305team.saferoute.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.c22ps305team.saferoute.api.ApiAuthConfig
import com.c22ps305team.saferoute.api.ApiReportConfig
import com.c22ps305team.saferoute.database.UserPreferences
import com.c22ps305team.saferoute.repository.UserAuthRepository
import com.c22ps305team.saferoute.repository.UserRepository

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val api = ApiReportConfig.getApiService()
        return UserRepository.getInstance(api)
    }

    fun provideUserAuthRepository(dataStore: DataStore<Preferences>): UserAuthRepository {
        val api = ApiAuthConfig.getApiService()
        val pref = UserPreferences.getInstance(dataStore)
        return UserAuthRepository(api, pref)
    }

}