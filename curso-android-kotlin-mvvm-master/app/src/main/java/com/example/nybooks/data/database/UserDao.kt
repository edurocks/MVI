package com.example.nybooks.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nybooks.data.response.LoginResponse

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserData(username : String) : LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}