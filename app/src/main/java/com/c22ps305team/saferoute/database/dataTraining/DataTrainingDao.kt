package com.c22ps305team.saferoute.database.dataTraining

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c22ps305team.saferoute.database.dataPredict.DataPredict

@Dao
interface DataTrainingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dataTraining: DataTraining)

    @Query("DELETE FROM dataTraining")
    fun delete()

    @Query("SELECT * FROM dataTraining")
    fun getDataTraining(): LiveData<List<DataTraining>>
}