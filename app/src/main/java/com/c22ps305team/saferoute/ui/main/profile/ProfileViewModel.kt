package com.c22ps305team.saferoute.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.c22ps305team.saferoute.repository.UserAuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: UserAuthRepository) : ViewModel() {



    fun fetchUser(): LiveData<String>{
        return repo.fetchUser().asLiveData()
    }

    fun deleteUser() = viewModelScope.launch {
        repo.deleteUser()
    }



}