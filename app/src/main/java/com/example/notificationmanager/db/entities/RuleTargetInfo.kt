package com.example.notificationmanager.db.entities

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable


data class RuleTargetInfo(
    val context: Context,
    val packageName: String,
    val display: String? = null,
    val drawable: Drawable? = null,
    var selected: Boolean = false) {

    var _displayName: String? = null
    val displayName: String
        get() {
            if (_displayName == null) {
                val packageManager = context.packageManager
                val app: ApplicationInfo =
                    packageManager.getApplicationInfo(packageName, 0)
//                _icon = packageManager.getApplicationIcon(app)
                _displayName = app.loadLabel(packageManager).toString()
            }
            return _displayName!!
        }

    /*
    var _icon: Drawable? = null
    val icon: Drawable
        get() {
            if (_icon == null) {
                val packageManager = context.packageManager
                val app: ApplicationInfo =
                    packageManager.getApplicationInfo(packageName, 0)
                _icon = packageManager.getApplicationIcon(app)
                _displayName = app.loadLabel(packageManager).toString()
            }
            return _icon!!
        }*/

    init {
        _displayName = display
//        if (display == null) {
//            val app: ApplicationInfo =
//                packageManager.getApplicationInfo(packageName, 0)
//            displayName = app.loadLabel(packageManager).toString()
//        } else {
//            displayName = display
//        }
//        _icon = drawable
//        if (drawable == null) {
//            val app: ApplicationInfo =
//                packageManager.getApplicationInfo(packageName, 0)
//            icon = packageManager.getApplicationIcon(app)
//        } else {
//            icon = drawable
//        }
    }
}
