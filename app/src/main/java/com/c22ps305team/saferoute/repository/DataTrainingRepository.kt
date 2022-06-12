package com.c22ps305team.saferoute.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.c22ps305team.saferoute.database.DataRoomDatabase
import com.c22ps305team.saferoute.database.dataTraining.DataTraining
import com.c22ps305team.saferoute.database.dataTraining.DataTrainingDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataTrainingRepository(application: Application) {
    private val mDataTrainingDao: DataTrainingDao
    private val excecutorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = DataRoomDatabase.getDatabase(application)
        mDataTrainingDao = db.dataTraining()
    }

    fun insertDataTraining(dataTraining: DataTraining) {
        excecutorService.execute {
            mDataTrainingDao.insert(dataTraining)
        }
    }

    fun deleteDataTraining() {
        excecutorService.execute {
            mDataTrainingDao.delete()
        }
    }

    fun getDataTraining(): LiveData<List<DataTraining>> = mDataTrainingDao.getDataTraining()
}