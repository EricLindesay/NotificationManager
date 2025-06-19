package com.example.notificationmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationmanager.db.DatabaseBuilder
import com.example.notificationmanager.db.NotificationInfo
import com.example.notificationmanager.db.PackageTypeCount
import com.example.notificationmanager.ui.compose.Utility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseBuilder.getInstance(application.applicationContext)
    private val dao = db.notificationDao()

    private val millisInDay: Long = 86400000L

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                .withZone(ZoneId.systemDefault())

    val activeNotificationsUnread: Flow<List<NotificationInfo>> = dao.getUnreadAllowedFlow()
    val activeNotifications: Flow<List<NotificationInfo>> = dao.getAllowedFlow()
    val silentNotificationsUnread: Flow<List<NotificationInfo>> = dao.getUnreadSilentFlow()
    val silentNotifications: Flow<List<NotificationInfo>> = dao.getSilentFlow()
    val packageTypeCounts: Flow<List<PackageTypeCount>> = dao.getPackageTypeCounts()

    val nonBlockedNotifications: Flow<List<NotificationInfo>> = dao.getNonBlockedFlow()

    val todayNotificationCounts: Flow<PackageTypeCount>
        get() {
            return dao.getDayInfo(Utility().millisToDateString(System.currentTimeMillis()))
        }

    val todayNotificationCountsPerPackage: Flow<List<PackageTypeCount>>
        get() {
            return dao.getDayInfoPerPackage(Utility().millisToDateString(System.currentTimeMillis()))
        }

    fun getDailyNotificationCounts(start: String, end: String) : Flow<List<PackageTypeCount>> {
        return dao.getDailyInfo(start, end)
    }

    fun getPastNDayInfo(endDate: String, n: Long) : Flow<List<PackageTypeCount>> {
        val inputDateTime: LocalDateTime = LocalDateTime.parse(endDate, formatter)
        val startDate: LocalDateTime = inputDateTime.minusDays(n)

        val startDateString: String = startDate.format(formatter).toString()

        return dao.getDailyInfo(startDateString, endDate)
    }

    fun getPastNDayInfoAsInts(endDate: String, n: Long) : Flow<List<List<Int>>> {
        val data = getPastNDayInfo(endDate, n)

        return data.map { ptc ->
            ptc.map { listOf(it.blockedCount, it.silentCount, it.allowedCount) }
        }
    }

    // cache the icons
    fun addNotification(notification: NotificationInfo) {
        viewModelScope.launch {
            dao.insertWithTimestamp(notification)
        }
    }
}

