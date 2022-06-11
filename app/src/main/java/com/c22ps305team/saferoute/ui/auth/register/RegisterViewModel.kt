package com.c22ps305team.saferoute.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c22ps305team.saferoute.api.ApiAuthConfig
import com.c22ps305team.saferoute.data.auth.AuthRequest
import com.c22ps305team.saferoute.data.auth.SignUpResponse
import com.c22ps305team.saferoute.repository.UserAuthRepository
import com.c22ps305team.saferoute.utils.Result
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel(private val repository: UserAuthRepository): ViewModel() {

    private var _registerResponse = MutableLiveData<SignUpResponse>()
    val registerResponse: LiveData<SignUpResponse> = _registerResponse


    /*fun register(username: String, password: String) = viewModelScope.launch {
        _registerResponse.value = Result.Loading()
        try {
            val response = repository.register(username, password)
            _registerResponse.value = Result.Success(response.body()!!)
            Log.e( "register: ", username)
        } catch (e: Exception){
            _registerResponse.value = Result.Error(e.message)
            Log.e( "RegisterViewModel", e.message.toString() )
        }
    }*/

    fun register(username: String, password: String){
        val client = ApiAuthConfig.getApiService().register(username, password)
        client.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                if (response.isSuccessful && response.body() != null){
                    _registerResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e( "RegisterError: ","${t.message}" )
            }
        })




    }



}