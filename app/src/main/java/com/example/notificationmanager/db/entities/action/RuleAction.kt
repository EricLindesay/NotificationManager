package com.example.notificationmanager.db.entities.action

import androidx.room.Entity

@Entity(primaryKeys = ["ruleId", "actionTypeId"])
data class RuleAction(
    val ruleId: Long,
    val actionTypeId: Long
)
