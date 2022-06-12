package com.c22ps305team.saferoute.ui.main.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c22ps305team.saferoute.api.ApiModelConfig
import com.c22ps305team.saferoute.database.dataPredict.DataPredict
import com.c22ps305team.saferoute.database.dataTraining.DataTraining
import com.c22ps305team.saferoute.repository.DataPredictRepository
import com.c22ps305team.saferoute.repository.DataTrainingRepository
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : ViewModel() {
    private val mDataPredictRepository: DataPredictRepository = DataPredictRepository(application)
    private val mDataTrainingRepository: DataTrainingRepository =
        DataTrainingRepository(application)

    private val _createModel = MutableLiveData<String>()
    val createModel: LiveData<String> = _createModel

    fun createModel(username: String) {
        val client = ApiModelConfig.getApiService().createModel(username)
        client.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("Response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Error Gan: ", "onFailure: ${t.message}")
            }

        })
    }

    fun trainModel(username: String, dataTraining: JsonObject) {
        val client = ApiModelConfig.getApiService().trainModel(username, dataTraining)
        client.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("Response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Error Gan: ", "onFailure: ${t.message}")
            }

        })

    }

    fun foreCast(username: String, dataPredict: JsonObject) {
        val client = ApiModelConfig.getApiService().foreCast(username, dataPredict)
        client.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("Response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Error Gan: ", "onFailure: ${t.message}")
            }

        })
    }

    fun insertData(dataPredict: DataPredict) {
        mDataPredictRepository.insertDataPredict(dataPredict)
    }

    fun insertDataTraining(dataTraining: DataTraining) {
        mDataTrainingRepository.insertDataTraining(dataTraining)
    }

    fun deleteData() {
        mDataPredictRepository.deletePeriodDataPredict()
    }

    fun deleteDataTraining() {
        mDataTrainingRepository.deleteDataTraining()
    }

    fun getAllData(): LiveData<List<DataPredict>> = mDataPredictRepository.getDataPredict()

    fun getAllDataTraining(): LiveData<List<DataTraining>> =
        mDataTrainingRepository.getDataTraining()

}