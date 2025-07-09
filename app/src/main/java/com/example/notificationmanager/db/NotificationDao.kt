package com.example.notificationmanager.db

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notificationmanager.db.entities.NotificationInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notificationinfo")
    suspend fun getAll(): List<NotificationInfo>

    @Query("SELECT * FROM notificationinfo WHERE type == 'NEW' ORDER BY created_at DESC")
    suspend fun getAllowed(): List<NotificationInfo>

    @Query("SELECT * FROM notificationinfo WHERE type == 'NEW' ORDER BY created_at DESC")
    fun getAllowedFlow(): Flow<List<NotificationInfo>>

    @Query("SELECT * FROM notificationinfo WHERE type == 'NEW' AND is_read = False ORDER BY created_at DESC")
    fun getUnreadAllowedFlow(): Flow<List<NotificationInfo>>

    @Query("SELECT * FROM notificationinfo WHERE type == 'SILENT' ORDER BY created_at DESC")
    suspend fun getSilent(): List<NotificationInfo>

    @Query("SELECT * FROM notificationinfo WHERE type == 'SILENT' ORDER BY created_at DESC")
    fun getSilentFlow(): Flow<List<NotificationInfo>>

    @Query("SELECT * FROM notificationinfo WHERE type == 'SILENT' AND is_read = False ORDER BY created_at DESC")
    fun getUnreadSilentFlow(): Flow<List<NotificationInfo>>

    @Query("SELECT * FROM notificationinfo WHERE type != 'BLOCKED' ORDER BY created_at DESC")
    fun getNonBlockedFlow(): Flow<List<NotificationInfo>>

    @Query("SELECT * FROM notificationinfo WHERE type == 'Blocked'")
    suspend fun getBlocked(): List<NotificationInfo>

    @Query("SELECT * FROM notificationinfo WHERE type == 'Blocked' ORDER BY created_at DESC")
    fun getBlockedFlow(): Flow<List<NotificationInfo>>

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE type == 'NEW'")
    suspend fun getNumAllowed(): Int

    @Query("""
        SELECT 
            package_name as identifier,
            SUM(CASE WHEN type = 'NEW' THEN 1 ELSE 0 END) AS allowed_count,
            SUM(CASE WHEN type = 'BLOCKED' THEN 1 ELSE 0 END) AS blocked_count,
            SUM(CASE WHEN type = 'SILENT' THEN 1 ELSE 0 END) AS silent_count
        FROM NotificationInfo
        GROUP BY identifier
        ORDER BY allowed_count+blocked_count+silent_count DESC
    """)
    fun getPackageTypeCounts(): Flow<List<PackageTypeCount>>

    @Query("""
        SELECT
            "" AS identifier,
            SUM(CASE WHEN type = 'NEW' THEN 1 ELSE 0 END) AS allowed_count,
            SUM(CASE WHEN type = 'BLOCKED' THEN 1 ELSE 0 END) AS blocked_count,
            SUM(CASE WHEN type = 'SILENT' THEN 1 ELSE 0 END) AS silent_count
        FROM NotificationInfo
        WHERE created_at > :time
        ORDER BY allowed_count+blocked_count+silent_count DESC
    """)
    // GROUP BY type
    fun getNotificationTypesAfterTime(time: Long): Flow<PackageTypeCount>

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE type == 'SILENT'")
    suspend fun getNumSilent(): Int

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE type == 'Blocked'")
    suspend fun getNumBlocked(): Int

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE package_name == :packageName AND title == :title AND text == :text AND created_at == :post_time")
    suspend fun alreadyPresent(packageName: String, title: String, text: String, post_time: Long): Int

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE package_name == :packageName AND created_at == :post_time")
    suspend fun timeInconsistency(packageName: String, post_time: Long): Int

    @Query("""
        SELECT
            "" AS identifier,
            SUM(CASE WHEN type = 'NEW' THEN 1 ELSE 0 END) AS allowed_count,
            SUM(CASE WHEN type = 'BLOCKED' THEN 1 ELSE 0 END) AS blocked_count,
            SUM(CASE WHEN type = 'SILENT' THEN 1 ELSE 0 END) AS silent_count
        FROM notificationinfo
        WHERE DATE(created_at / 1000, 'unixepoch') == :day
        ORDER BY created_at
    """)
//            DATE(created_at / 1000, 'unixepoch') AS day,
//            COUNT(*) AS count
    // Day is a string like "2025-07-17"
    fun getDayInfo(day: String) : Flow<PackageTypeCount>

    @Query("""
        SELECT
            package_name AS identifier,
            SUM(CASE WHEN type = 'NEW' THEN 1 ELSE 0 END) AS allowed_count,
            SUM(CASE WHEN type = 'BLOCKED' THEN 1 ELSE 0 END) AS blocked_count,
            SUM(CASE WHEN type = 'SILENT' THEN 1 ELSE 0 END) AS silent_count
        FROM notificationinfo
        WHERE DATE(created_at / 1000, 'unixepoch') == :day
        GROUP BY identifier
        ORDER BY allowed_count+blocked_count+silent_count DESC
    """)
//            DATE(created_at / 1000, 'unixepoch') AS day,
//            COUNT(*) AS count
    // Day is a string like "2025-07-17"
    fun getDayInfoPerPackage(day: String) : Flow<List<PackageTypeCount>>

    @Query("""    
        SELECT
            DATE(created_at / 1000, 'unixepoch') AS identifier,
            SUM(CASE WHEN type = 'NEW' THEN 1 ELSE 0 END) AS allowed_count,
            SUM(CASE WHEN type = 'BLOCKED' THEN 1 ELSE 0 END) AS blocked_count,
            SUM(CASE WHEN type = 'SILENT' THEN 1 ELSE 0 END) AS silent_count
        FROM notificationinfo
        WHERE identifier BETWEEN :start AND :end
        GROUP BY identifier
        ORDER BY identifier
    """)
    fun getDailyInfo(start: String, end: String) : Flow<List<PackageTypeCount>>

//    @Query("""
//        SELECT
//            SUM(CASE WHEN type = 'NEW' THEN 1 ELSE 0 END) AS allowed_count,
//            SUM(CASE WHEN type = 'BLOCKED' THEN 1 ELSE 0 END) AS blocked_count,
//            SUM(CASE WHEN type = 'SILENT' THEN 1 ELSE 0 END) AS silent_count
//        FROM notificationinfo
//        WHERE DATE(created_at / 1000, 'unixepoch') BETWEEN :start AND :end
//        GROUP BY DATE(created_at / 1000, 'unixepoch')
//        ORDER BY DATE(created_at / 1000, 'unixepoch')
//    """)
    // Need to get this so it can provide something readable by the bar charts
//    fun getDailyInfo2(start: String, end: String) : Flow<List<List<Int>>>

    @Insert
    suspend fun insertAll(vararg notifications: NotificationInfo)

    @Insert
    suspend fun insert(notification: NotificationInfo)

    suspend fun insertWithTimestamp(data: NotificationInfo) {
        data.createdAt = System.currentTimeMillis()
        if (alreadyPresent(data.packageName, data.title, data.text, data.createdAt!!) > 0)
            return

        Log.d("NotificationCatcher", "Set time created at")
        insert(data)
    }

    suspend fun insertWithTimestamp(data: NotificationInfo, time: Long) {
        data.createdAt = time
        if (alreadyPresent(data.packageName, data.title, data.text, data.createdAt!!) > 0)
            return

        // Assume that the same package didn't post two notifications simulatenously
        if (timeInconsistency(data.packageName, data.createdAt!!) > 0)
            return

        Log.d("NotificationCatcher", "Set time created at")
        insert(data)
    }

    @Query("""
        UPDATE notificationinfo
        SET is_read = True
        WHERE
            package_name == :packageName AND
            created_at == :time
    """)
    suspend fun setAsRead(packageName: String, time: Long)

    @Query("""
        UPDATE notificationinfo
        SET is_read = True
    """)
    suspend fun setAllRead()

    // Probs want
    // - Get by package
}