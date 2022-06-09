package com.c22ps305team.saferoute.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userData: UserData)

    @Delete
    fun delete(userData: UserData)
}