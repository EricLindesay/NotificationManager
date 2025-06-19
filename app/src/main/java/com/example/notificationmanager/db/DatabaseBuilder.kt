package com.example.notificationmanager.db

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context) : AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) : AppDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "app-database13"
        ).build()

        return db
    }

}