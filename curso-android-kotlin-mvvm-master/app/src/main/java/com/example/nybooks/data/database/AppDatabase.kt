package com.example.nybooks.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        var sInstance: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            if (sInstance == null){
                synchronized(AppDatabase::class.java){
                    if (sInstance == null){
                        sInstance = Room.databaseBuilder(context, AppDatabase::class.java, "room-db").build()
                    }
                }
            }

            return sInstance!!
        }
    }

    abstract fun userDao() : UserDao

}