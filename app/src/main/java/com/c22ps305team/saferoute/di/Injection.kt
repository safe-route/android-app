package com.c22ps305team.saferoute.di

import android.content.Context
import com.c22ps305team.saferoute.api.ApiReportConfig
import com.c22ps305team.saferoute.repository.UserRepository

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val api = ApiReportConfig.getApiService()
        return UserRepository.getInstance(api)
    }

}