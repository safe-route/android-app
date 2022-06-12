package com.c22ps305team.saferoute.ui.main.reportCrime

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c22ps305team.saferoute.data.ReportCrimeResponse
import com.c22ps305team.saferoute.repository.UserRepository
import com.c22ps305team.saferoute.utils.Result
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class CrimeReportViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _reportResponse = MutableLiveData<Result<ReportCrimeResponse>>()
    val reportResponse: LiveData<Result<ReportCrimeResponse>> = _reportResponse

    fun uploadCrimeReport(report: JsonObject) =
        viewModelScope.launch {
            _reportResponse.value = Result.Loading()
            try {
                userRepository.uploadCrimeReport(report)
                //_reportResponse.value = Result.Success(response)
                //Log.e( "uploadCrimeReport: ", response.body()!!.toString() )
            } catch (e: Exception){
                _reportResponse.value = Result.Error(e.message.toString())
                Log.e( "uploadCrimeReport: ", e.message.toString() )
            }
        }

}