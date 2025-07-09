package com.example.notificationmanager.db.entities.action

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RuleActionAttribute(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val value: String,
)