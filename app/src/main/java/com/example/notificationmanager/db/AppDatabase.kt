package com.example.notificationmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotificationInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}