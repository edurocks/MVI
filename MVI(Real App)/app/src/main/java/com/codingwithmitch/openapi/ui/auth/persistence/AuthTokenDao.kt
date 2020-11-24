package com.codingwithmitch.openapi.ui.auth.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingwithmitch.openapi.ui.auth.model.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authToken: AuthToken): Long

    @Query("UPDATE auth_token SET token = null WHERE account_pk= :pk")
    fun nullifyToken(pk: Int): Int

    @Query("SELECT * FROM auth_token WHERE account_pk = :pk")
    suspend fun searchByPk(pk: Int): AuthToken?
}