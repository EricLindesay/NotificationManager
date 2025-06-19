package com.example.notificationmanager.db

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log

class DBUtility {
    fun getAppName(context: Context, packageName: String) : String {
        val packageManager = context.packageManager
        var applicationInfo : ApplicationInfo?
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }

        val applicationName: String =
            applicationInfo?.let { packageManager.getApplicationLabel(it).toString() } ?: packageName

        Log.d("NotificationInfo", "Got application name: "+applicationName)

        return applicationName
    }

    fun getPackageIcon(context: Context, packageName: String) : Drawable {
        val packageManager = context.packageManager
        return packageManager.getApplicationIcon(packageName)
    }

}