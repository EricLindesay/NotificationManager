package com.example.notificationmanager.db.entities.condition

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.notificationmanager.db.entities.RuleType

@Entity
data class RuleConditionType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: RuleConditionEnum,
) : RuleType() {
    override fun getDisplayName(): String {
        return RuleConditionEnumTypeConverter().fromType(name)
    }

    override fun getDisplayDesc(): String {
        // Use the attributes for this
        TODO("Not yet implemented")
    }
}

enum class RuleConditionEnum {
    CONTAINS,        // text
    MATCH_REGEX,     // regex
    HAS_NUMBER,      // number
    AFTER_TIME,      // start_time
    BEFORE_TIME,     // end_time
    BETWEEN_TIME,    // start_time, end_time
    AFTER_DATE,      // start_date
    BEFORE_DATE,     // end_date
    BETWEEN_DATES,   // start_date, end_date
    AFTER_DAY,       // start_day
    BEFORE_DAY,      // end_day
    BETWEEN_DAY,     // start_day, end_day
    ACTIVATES,       // rule
    TYPE,            // type e.g. SOCIAL_MEDIA, etc

    // SCREEN_ON,
    // SCREEN_OFF,
    // CHARGING_WIRED,
    // CHARGING_WIRELESS,
    // NOT_CHARGING
}

class RuleConditionEnumTypeConverter {
    @TypeConverter
    fun toType(value: String) = enumValueOf<RuleConditionEnum>(value)

    @TypeConverter
    fun fromType(value: RuleConditionEnum) = value.name
}