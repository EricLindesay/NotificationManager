package com.example.notificationmanager.db.entities.target

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.notificationmanager.db.entities.RuleType

@Entity
data class RuleTargetType(
    val name: RuleTargetTypeEnum,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : RuleType() {

    override fun getDisplayName(): String {
        return RuleTargetTypeEnumTypeConverter().fromType(name)
    }

    @Ignore
    override fun getDisplayDesc(): String {
        return ""
    }
}

// package name or custom like "All Apps"
// Package
// All Apps
// Group - SOCIAL_MEDIA, etc


// have another entity which has dispaly name, icon and visible for use by the app.


enum class RuleTargetTypeEnum {
    PACKAGE,  // package_name
    ALL,
    CATEGORY,    // SOCIAL_MEDIA, etc (https://developer.android.com/reference/android/app/Notification#constants)
}

class RuleTargetTypeEnumTypeConverter {
    @TypeConverter
    fun toType(value: String) = enumValueOf<RuleTargetTypeEnum>(value)

    @TypeConverter
    fun fromType(value: RuleTargetTypeEnum) = value.name
}
