package com.example.notificationmanager.db

import androidx.room.ColumnInfo

data class TestClass (
    @ColumnInfo(name = "day") val day: String,
    @ColumnInfo(name = "count") val count: Int,
) {
}