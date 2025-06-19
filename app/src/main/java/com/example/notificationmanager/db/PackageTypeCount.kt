package com.example.notificationmanager.db

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Ignore

data class PackageTypeCount (
    @ColumnInfo(name = "identifier") val identifier: String,
    @ColumnInfo(name = "allowed_count") val allowedCount: Int,
    @ColumnInfo(name = "blocked_count") val blockedCount: Int,
    @ColumnInfo(name = "silent_count") val silentCount: Int
) {
    @Ignore
    val icon: Drawable? = null

    @Ignore
    val total: Int = allowedCount+blockedCount+silentCount
}