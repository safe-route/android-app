package com.c22ps305team.saferoute.database.dataPredict

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataPredictDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dataPredict: DataPredict)

    @Query("DELETE FROM dataPredict")
    fun delete()

    @Query("SELECT * FROM datapredict")
    fun getUserData(): LiveData<List<DataPredict>>
}