package com.c22ps305team.saferoute.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.c22ps305team.saferoute.database.UserData
import com.c22ps305team.saferoute.database.UserDataDao
import com.c22ps305team.saferoute.database.UserDataRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserDataRepository(application: Application) {
    private val mUserDataDao: UserDataDao
    private val excecutorService: ExecutorService = Executors.newSingleThreadExecutor()


    init {
        val db = UserDataRoomDatabase.getDatabase(application)
        mUserDataDao = db.userDataDao()
    }

    fun insert(userData: UserData) {
        excecutorService.execute {
            mUserDataDao.insert(userData)
        }
    }

    fun getUserData(): LiveData<List<UserData>> = mUserDataDao.getUserData()

}