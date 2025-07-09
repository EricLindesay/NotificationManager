package com.example.notificationmanager.db.entities.condition

import androidx.room.Entity

@Entity(primaryKeys = ["ruleId", "conditionTypeId"])
data class RuleCondition(
    val ruleId: Long,
    val conditionTypeId: Long
)
