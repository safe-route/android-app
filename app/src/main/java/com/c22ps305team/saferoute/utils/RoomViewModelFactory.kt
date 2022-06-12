package com.c22ps305team.saferoute.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.ui.main.home.HomeViewModel
import java.lang.IllegalArgumentException

class RoomViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(mApplication) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: RoomViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): RoomViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RoomViewModelFactory::class.java) {
                    INSTANCE = RoomViewModelFactory(application)
                }
            }
            return INSTANCE as RoomViewModelFactory
        }
    }
}