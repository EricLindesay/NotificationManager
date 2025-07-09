package com.example.notificationmanager.db.entities.action

import androidx.room.Entity

@Entity(primaryKeys = ["ruleId", "actionAttributeId"])
data class RuleActionAttributeLink(
    val ruleId: Long,
    val actionAttributeId: Long
)
