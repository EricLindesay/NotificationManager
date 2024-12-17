package com.example.notificationmanager

import android.graphics.drawable.Drawable
import java.io.Serializable

data class NotificationInfo(
    val packageName: String?,
    val title: String?,
    val text: String?,
    val color: Int?,
    var icon: Drawable? = null)
    : Serializable