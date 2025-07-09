package com.example.notificationmanager.db.entities.target

import androidx.room.Entity

@Entity(primaryKeys = ["ruleId", "targetTypeId"])
data class RuleTarget(
    val ruleId: Long,
    val targetTypeId: Long
)
