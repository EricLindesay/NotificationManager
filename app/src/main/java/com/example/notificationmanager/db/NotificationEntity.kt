package com.example.notificationmanager.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.Serializable

//@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name="package_name") val packageName: String?,
    @ColumnInfo(name="title") val title: String?,
    @ColumnInfo(name="text") val text: String?,
    @ColumnInfo(name="color") val color: Int?,
    @TypeConverters(Converters::class) val type: types
//    type
) : Serializable

//class Converters {
//    @TypeConverter
//    fun toType(value: String) = enumValueOf<types>(value)
//
//    @TypeConverter
//    fun fromType(value: types) = value.name
//}
