package com.example.notificationmanager.db.entities

import androidx.room.Ignore

abstract class RuleType {
    abstract fun getDisplayName(): String

    abstract fun getDisplayDesc(): String
}