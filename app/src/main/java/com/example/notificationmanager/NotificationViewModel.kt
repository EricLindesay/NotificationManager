package com.example.notificationmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.notificationmanager.db.AppDatabase
import com.example.notificationmanager.db.NotificationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "app-database"
    ).build()

    private val dao = db.notificationDao()

    val activeNotifications: Flow<ArrayList<NotificationInfo?>> = dao.getAllowedFlow()
    val silentNotifications: Flow<ArrayList<NotificationInfo?>> = dao.getSilentFlow()

    fun addNotification(notification: NotificationInfo) {
        viewModelScope.launch {
            dao.insert(notification)
        }
    }
}

