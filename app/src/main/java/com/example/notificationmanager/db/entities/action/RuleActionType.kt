package com.example.notificationmanager.db.entities.action

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.notificationmanager.db.entities.RuleType

@Entity
data class RuleActionType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: RuleActionTypeEnum,
) : RuleType() {

    override fun getDisplayName(): String {
        return RuleActionTypeEnumTypeConverter().fromType(name)
    }

    override fun getDisplayDesc(): String {
        // Use the attributes for this
        TODO("Not yet implemented")
    }
}

enum class RuleActionTypeEnum {
    ALLOW,
    BLOCK,
    SILENCE,
    TEMP_MUTE,  // duration
    PLAY,       // sound, sound_duration
    VIBRATE,    // vibrate_duration, intensity if possible
    DELAY,      // delay_duration
    SUBSTITUTE, // pattern, replacement
}

class RuleActionTypeEnumTypeConverter {
    @TypeConverter
    fun toType(value: String) = enumValueOf<RuleActionTypeEnum>(value)

    @TypeConverter
    fun fromType(value: RuleActionTypeEnum) = value.name
}
