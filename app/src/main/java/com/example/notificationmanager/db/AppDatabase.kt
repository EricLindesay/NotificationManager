package com.example.notificationmanager.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notificationmanager.db.entities.Converters
import com.example.notificationmanager.db.entities.NotificationInfo
import com.example.notificationmanager.db.entities.Rule
import com.example.notificationmanager.db.entities.action.RuleAction
import com.example.notificationmanager.db.entities.action.RuleActionAttribute
import com.example.notificationmanager.db.entities.action.RuleActionAttributeLink
import com.example.notificationmanager.db.entities.action.RuleActionType
import com.example.notificationmanager.db.entities.action.RuleActionTypeEnumTypeConverter
import com.example.notificationmanager.db.entities.action.RuleConditionAttributeLink
import com.example.notificationmanager.db.entities.action.RuleTargetAttributeLink
import com.example.notificationmanager.db.entities.condition.RuleCondition
import com.example.notificationmanager.db.entities.condition.RuleConditionAttribute
import com.example.notificationmanager.db.entities.condition.RuleConditionEnumTypeConverter
import com.example.notificationmanager.db.entities.condition.RuleConditionType
import com.example.notificationmanager.db.entities.target.RuleTarget
import com.example.notificationmanager.db.entities.target.RuleTargetAttribute
import com.example.notificationmanager.db.entities.target.RuleTargetType
import com.example.notificationmanager.db.entities.target.RuleTargetTypeEnumTypeConverter

@Database(
    entities = [
        NotificationInfo::class,
        Rule::class,

        RuleAction::class,
        RuleActionAttributeLink::class,
        RuleActionType::class,
        RuleActionAttribute::class,

        RuleConditionType::class,
        RuleConditionAttribute::class,
        RuleCondition::class,
        RuleConditionAttributeLink::class,

        RuleTargetType::class,
        RuleTarget::class,
        RuleTargetAttribute::class,
        RuleTargetAttributeLink::class,
        ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 2, to = 3) // , spec=MigrationSpec::class
    ],
    exportSchema = true)
@TypeConverters(
    Converters::class,
    RuleActionTypeEnumTypeConverter::class,
    RuleConditionEnumTypeConverter::class,
    RuleTargetTypeEnumTypeConverter::class
    )
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun ruleDao(): RuleDao
}