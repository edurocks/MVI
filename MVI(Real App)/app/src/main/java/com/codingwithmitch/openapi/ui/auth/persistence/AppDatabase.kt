package com.codingwithmitch.openapi.ui.auth.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingwithmitch.openapi.ui.auth.model.AccountProperties
import com.codingwithmitch.openapi.ui.auth.model.AuthToken

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAuthToken(): AuthTokenDao
    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        const val DATABASE_NAME = "app_db"
    }
}