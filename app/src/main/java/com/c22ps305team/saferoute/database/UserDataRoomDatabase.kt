package com.c22ps305team.saferoute.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserData::class], version = 1)
abstract class UserDataRoomDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

    companion object {
        @Volatile
        private var INSTANCE: UserDataRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDataRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserDataRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDataRoomDatabase::class.java, "userdata_database"
                    )
                        .build()
                }
            }
            return INSTANCE as UserDataRoomDatabase
        }
    }
}