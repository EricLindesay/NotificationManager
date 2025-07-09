package com.example.notificationmanager.db.entities.target

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RuleTargetAttribute(
    @PrimaryKey(autoGenerate=true) val id: Long = 0,
    val name: String,
    val value: String,
)