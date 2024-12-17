package com.example.notificationmanager

import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Base64
import com.example.notificationmanager.MainActivity.Companion.NEW_NOTIF
import com.example.notificationmanager.MainActivity.Companion.PACKAGE
import com.example.notificationmanager.MainActivity.Companion.SETUP
import java.io.ByteArrayOutputStream

class NotificationCatcher : NotificationListenerService() {
    override fun onListenerConnected() {
        var ans: ArrayList<NotificationInfo> = ArrayList()
        for (n: StatusBarNotification in activeNotifications) {
//            ans.add(n.packageName)
            val title: String? = n.notification.extras.getString(Notification.EXTRA_TITLE)
            val text: String? = n.notification.extras.getString(Notification.EXTRA_TEXT)
//            var icon: Icon? = n.notification.smallIcon
//            if (icon == null) {
//                icon = n.notification.getLargeIcon()
//            } else {
//                println(icon)
//            }
            val color: Int = n.notification.color

//            val bitmap: Bitmap = (icon?.loadDrawable(this) as BitmapDrawable).bitmap
//            val drawable: Drawable? = icon?.loadDrawable(this)
//            val bitmap: String?
//            if (drawable != null)
//                bitmap = drawableToBase64(drawable)
//            else
//                bitmap = null

            ans.add(NotificationInfo(n.packageName, title, text, color))
            /*
            if (n.packageName == "com.whatsapp") {
                println("Notification " + n.packageName)
                if (n.notification.actions != null) {
                    for (action in n.notification.actions) {
                        print("Action: ")
                        for (key in action.extras.keySet()) {
                            print(action.extras.keySet() + " ")
                        }
                        println()
                        println("Title: "+action.title)
                    }
                }
                for (key: String in n.notification.extras.keySet()) {
                    println(key)
                }
                println(n.notification.extras.getString(Notification.EXTRA_TEXT))
                println(n.notification.extras.getString(Notification.EXTRA_TITLE))
                println(n.notification.extras.getString(Notification.EXTRA_SUB_TEXT))

            }
            println(n.packageName+ ": "+ n.notification.color)
             */
        }
        val intent = Intent(PACKAGE + SETUP)
//        intent.putStringArrayListExtra("Notification Code", ans)
        intent.putExtra("Notification Code", ans)
        sendBroadcast(intent)
        super.onListenerConnected()
    }

    fun drawableToBase64(drawable: Drawable): String {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth.takeIf { it > 0 } ?: 100, // Fallback size if width/height is -1
            drawable.intrinsicHeight.takeIf { it > 0 } ?: 100,
            Bitmap.Config.ARGB_8888
        )

        // Render the drawable onto a Canvas backed by the Bitmap
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        // Compress the Bitmap into a byte array using PNG format
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        // Encode the byte array to Base64
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    fun drawableToBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            // If it's already a BitmapDrawable, return its bitmap
            drawable.bitmap
        } else {
            // Otherwise, render the drawable into a Bitmap
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth.takeIf { it > 0 } ?: 1,
                drawable.intrinsicHeight.takeIf { it > 0 } ?: 1,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }



    override fun onNotificationPosted(sbn: StatusBarNotification) {
        println("Pre send 1")
        val intent = Intent(PACKAGE+NEW_NOTIF)
        intent.putExtra("Notification Code", sbn.key)
        sendBroadcast(intent)
        println("Sent success")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
//        int notificationCode = matchNotificationCode(sbn);

//        if(notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {

        val activeNotifications = this.activeNotifications

        if (activeNotifications != null && activeNotifications.size > 0) {
            for (i in activeNotifications.indices) {
//                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                val intent = Intent(PACKAGE)
                intent.putExtra("Notification Code", activeNotifications[i].notification)
                sendBroadcast(intent)
                break
                //                    }
//                }
            }
        }
    }

    fun getNotification(id: String) : StatusBarNotification {
        return getActiveNotifications(arrayOf(id))[0]
    }
}