package com.example.notificationmanager.db.entities.action

import androidx.room.Entity

@Entity(primaryKeys = ["ruleId", "conditionAttributeId"])
data class RuleConditionAttributeLink(
    val ruleId: Long,
    val conditionAttributeId: Long
)
