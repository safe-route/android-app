package com.c22ps305team.saferoute.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c22ps305team.saferoute.repository.UserAuthRepository

class SplashViewModel(private val repository: UserAuthRepository) : ViewModel() {

    fun fetchUser(): LiveData<String> {
        return repository.fetchToken().asLiveData()
    }

}