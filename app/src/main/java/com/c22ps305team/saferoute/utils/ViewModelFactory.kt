package com.c22ps305team.saferoute.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.di.Injection
import com.c22ps305team.saferoute.repository.UserAuthRepository
import com.c22ps305team.saferoute.repository.UserRepository
import com.c22ps305team.saferoute.ui.auth.login.LoginViewModel
import com.c22ps305team.saferoute.ui.auth.register.RegisterActivity
import com.c22ps305team.saferoute.ui.auth.register.RegisterViewModel
import com.c22ps305team.saferoute.ui.main.reportCrime.CrimeReportViewModel
import com.c22ps305team.saferoute.ui.splash.SplashViewModel
import java.lang.IllegalArgumentException


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_account")

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val userAuthRepository: UserAuthRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userAuthRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userAuthRepository) as T
            }
            modelClass.isAssignableFrom(CrimeReportViewModel::class.java) -> {
                CrimeReportViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(userAuthRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }


    companion object {
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory{
            return INSTANCE ?: synchronized(this){
                ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideUserAuthRepository(context.dataStore)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

}