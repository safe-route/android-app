package com.c22ps305team.saferoute.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userData: UserData)

    @Delete
    fun delete(userData: UserData)

    @Query("SELECT * FROM userdata")
    fun getUserData(): LiveData<List<UserData>>
}