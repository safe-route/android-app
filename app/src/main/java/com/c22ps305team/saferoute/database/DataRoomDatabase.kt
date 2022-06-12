package com.c22ps305team.saferoute.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.c22ps305team.saferoute.database.dataPredict.DataPredict
import com.c22ps305team.saferoute.database.dataPredict.DataPredictDao
import com.c22ps305team.saferoute.database.dataTraining.DataTraining
import com.c22ps305team.saferoute.database.dataTraining.DataTrainingDao

@Database(entities = [DataTraining::class, DataPredict::class], version = 1)
abstract class DataRoomDatabase : RoomDatabase() {
    abstract fun dataPredict(): DataPredictDao
    abstract fun dataTraining(): DataTrainingDao

    companion object {
        @Volatile
        private var INSTANCE: DataRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): DataRoomDatabase {
            if (INSTANCE == null) {
                synchronized(DataRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DataRoomDatabase::class.java, "dataDb"
                    )
                        .build()
                }
            }
            return INSTANCE as DataRoomDatabase
        }
    }
}