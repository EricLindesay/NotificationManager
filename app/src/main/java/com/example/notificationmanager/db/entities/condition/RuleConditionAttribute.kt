package com.example.notificationmanager.db.entities.condition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RuleConditionAttribute(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val value: String,
)

// This is stuff like contains_text, "Bob" for name and value
// and start_date: 10020, end_date: !001202 if you have a condition abotu days