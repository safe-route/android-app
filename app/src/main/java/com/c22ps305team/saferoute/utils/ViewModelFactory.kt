package com.c22ps305team.saferoute.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.di.Injection
import com.c22ps305team.saferoute.repository.UserRepository
import com.c22ps305team.saferoute.ui.main.reportCrime.CrimeReportViewModel
import java.lang.IllegalArgumentException


class ViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CrimeReportViewModel::class.java) -> {
                CrimeReportViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }



    companion object {
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory{
            return INSTANCE ?: synchronized(this){
                ViewModelFactory(Injection.provideUserRepository(context)).also {
                    INSTANCE = it
                }
            }
        }
    }

}