package com.example.notificationmanager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notificationinfo")
    fun getAll(): ArrayList<NotificationInfo?>

    @Query("SELECT * FROM notificationinfo WHERE type == 'NEW'")
    fun getAllowed(): ArrayList<NotificationInfo?>

    @Query("SELECT * FROM notificationinfo WHERE type == 'NEW'")
    fun getAllowedFlow(): Flow<ArrayList<NotificationInfo?>>

    @Query("SELECT * FROM notificationinfo WHERE type == 'SILENT'")
    fun getSilent(): ArrayList<NotificationInfo?>

    @Query("SELECT * FROM notificationinfo WHERE type == 'SILENT'")
    fun getSilentFlow(): Flow<ArrayList<NotificationInfo?>>

    @Query("SELECT * FROM notificationinfo WHERE type == 'Blocked'")
    fun getBlocked(): ArrayList<NotificationInfo?>

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE type == 'NEW'")
    fun getNumAllowed(): Int

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE type == 'SILENT'")
    fun getNumSilent(): Int

    @Query("SELECT COUNT(*) FROM notificationinfo WHERE type == 'Blocked'")
    fun getNumBlocked(): Int

    @Insert
    fun insertAll(vararg notifications: NotificationInfo)

    @Insert
    fun insert(notification: NotificationInfo)

    // Probs want
    // - Get by package

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<NotificationEntity>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
//    @Insert
//    fun insertAll(vararg users: User)
//
//    @Delete
//    fun delete(user: User)

}