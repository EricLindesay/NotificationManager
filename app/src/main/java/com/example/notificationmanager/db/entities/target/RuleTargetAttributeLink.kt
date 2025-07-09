package com.example.notificationmanager.db.entities.action

import androidx.room.Entity

@Entity(primaryKeys = ["ruleId", "targetAttributeId"])
data class RuleTargetAttributeLink(
    val ruleId: Long,
    val targetAttributeId: Long
)
