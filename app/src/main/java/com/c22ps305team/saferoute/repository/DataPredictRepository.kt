package com.c22ps305team.saferoute.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.c22ps305team.saferoute.database.dataPredict.DataPredict
import com.c22ps305team.saferoute.database.dataPredict.DataPredictDao
import com.c22ps305team.saferoute.database.DataRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataPredictRepository(application: Application) {
    private val mDataPredictDao: DataPredictDao
    private val excecutorService: ExecutorService = Executors.newSingleThreadExecutor()


    init {
        val db = DataRoomDatabase.getDatabase(application)
        mDataPredictDao = db.dataPredict()
    }

    fun insertDataPredict(dataPredict: DataPredict) {
        excecutorService.execute {
            mDataPredictDao.insert(dataPredict)
        }
    }

    fun deletePeriodDataPredict() {
        excecutorService.execute {
            mDataPredictDao.delete()
        }
    }

    fun getDataPredict(): LiveData<List<DataPredict>> = mDataPredictDao.getUserData()

}