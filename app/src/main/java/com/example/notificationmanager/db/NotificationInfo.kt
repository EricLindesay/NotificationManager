package com.example.notificationmanager.db

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.Serializable

@Entity
data class NotificationInfo(
    @ColumnInfo(name="package_name") val packageName: String,
    @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="text") val text: String,

    @ColumnInfo(name="color") val color: Int = Color.WHITE,
    @ColumnInfo(name="is_read") val isRead: Boolean = false,
    @ColumnInfo(name="type") var type: types = types.NEW,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name="created_at") var createdAt: Long? = null
)
    : Serializable {

    @Ignore
    private var icon: Drawable? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NotificationInfo) return false

        if (packageName != other.packageName) return false
        if (title != other.title) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode() ?: 0
        result = 31 * result + (title.hashCode() ?: 0)
        result = 31 * result + (text.hashCode() ?: 0)
        return result
    }

    fun setPackageIcon(context: Context) {
        if (icon != null) {
            // Only set the icon if it doesn't already exist
            return;
        }

        val packageManager = context.packageManager
        icon = packageManager.getApplicationIcon(packageName)
    }

    fun getIcon(context: Context) : Drawable? {
        setPackageIcon(context)
        return icon
    }

    fun setIcon(newIcon: Drawable) {
        icon = newIcon
    }

    fun getAppName(context: Context) : String {
        val packageManager = context.packageManager
        var applicationInfo : ApplicationInfo?
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }

        val applicationName: String =
            applicationInfo?.let { packageManager.getApplicationLabel(it).toString() } ?: packageName

        Log.d("NotificationInfo", "Got application name: "+applicationName)

        return applicationName
    }
}

enum class types {
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
