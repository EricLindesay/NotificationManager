package com.example.notificationmanager.db

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.Serializable

@Entity
data class NotificationInfo(
//    val packageName: String?,
//    val title: String?,
//    val text: String?,
//    val color: Int?,
//    var icon: Drawable? = null,
//    var type: types = types.NEW
    @ColumnInfo(name="package_name") val packageName: String?,
    @ColumnInfo(name="title") val title: String?,
    @ColumnInfo(name="text") val text: String?,
    @ColumnInfo(name="color") val color: Int?,

    @TypeConverters(Converters::class) var type: types = types.NEW,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @Ignore var icon: Drawable? = null
)
    : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NotificationInfo) return false

        if (packageName != other.packageName) return false
        if (title != other.title) return false
        if (text != other.text) return false

        return true
    }
    }

enum class types {
    SEPARATOR_NEW,
    SEPARATOR_SILENT,
    NEW,
    SILENT,
    BLOCKED,
}

class Converters {
    @TypeConverter
    fun toType(value: String) = enumValueOf<types>(value)

    @TypeConverter
    fun fromType(value: types) = value.name
}
