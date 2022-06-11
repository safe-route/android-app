package com.c22ps305team.saferoute.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c22ps305team.saferoute.api.ApiAuthConfig
import com.c22ps305team.saferoute.data.auth.AuthRequest
import com.c22ps305team.saferoute.data.auth.LoginResponse
import com.c22ps305team.saferoute.repository.UserAuthRepository
import com.c22ps305team.saferoute.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserAuthRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse


    fun login(user: String, password: String) = viewModelScope.launch {
        _loginResponse.value = Result.Loading()
        try {
            val response = repository.login(user, password)
            _loginResponse.value = Result.Success(response.body()!!)
            Log.e("Login", response.toString())
        } catch (e: Exception) {
            _loginResponse.value = Result.Error(e.message.toString())
            Log.e("LoginViewModel", e.message.toString())
        }
    }

    fun saveToken(token: String) = viewModelScope.launch {
        repository.saveToken(token)
    }

    fun saveUser(user: String) = viewModelScope.launch {
        repository.saveUser(user)
    }




}