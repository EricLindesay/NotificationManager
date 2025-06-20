package com.example.notificationmanager

import android.app.Notification
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Base64
import android.util.Log
import com.example.notificationmanager.db.DatabaseBuilder
import com.example.notificationmanager.db.NotificationDao
//import com.example.notificationmanager.MainActivity.Companion.NEW_NOTIF
//import com.example.notificationmanager.MainActivity.Companion.PACKAGE
//import com.example.notificationmanager.MainActivity.Companion.SETUP
import com.example.notificationmanager.db.NotificationInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class NotificationCatcher : NotificationListenerService() {

    lateinit var dao: NotificationDao

    override fun onListenerConnected() {
        // Filter to remove duplicates
        val db = DatabaseBuilder.getInstance(this)

        dao = db.notificationDao()

        Log.d("NotificationCatcher", "Catcher connected")
        for (n: StatusBarNotification in activeNotifications) {
            saveNotification(n)
        }
//        val intent = Intent(PACKAGE + SETUP)
//        intent.putParcelableArrayListExtra("Notifications", ans)
//        intent.putStringArrayListExtra("Notification Code", ans)
//        intent.putExtra("Notification Code", ans)
//        sendBroadcast(intent)

        super.onListenerConnected()
    }
//            var icon: Icon? = n.notification.smallIcon
//            if (icon == null) {
//                icon = n.notification.getLargeIcon()
//            } else {
//                println(icon)
//            }

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
        saveNotification(sbn)
    }

    fun saveNotification(sbn: StatusBarNotification) {
//            ans.add(n.packageName)
        val title: String = sbn.notification.extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text: String = sbn.notification.extras.getString(Notification.EXTRA_TEXT) ?: ""
        val color: Int = sbn.notification.color

        val notification = NotificationInfo(sbn.packageName, title, text, color)

        // StatusBarNotification.Notification.Category is an interesting thign to consider

        // Don't add completely blank notifications
        if (title.isBlank() && text.isBlank()) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("NotificationCatcher", "Adding notification: "+sbn.packageName)
            dao.insertWithTimestamp(notification, sbn.postTime)
            Log.d("NotificationCatcher", sbn.packageName+" added")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // find it and set is read to true
        // could use the id in the db, then if there is a duplicate then you need to edit the message somehow
        // and to delete it, jsut find the id for the package
        // the id isn't unqiue but it shouldn't be reused byt he package hopefully
        // or find soething which wont be
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("NotificationCatcher", "Setting notification as read: "+sbn.packageName)
            dao.setAsRead(sbn.packageName, sbn.postTime)
            Log.d("NotificationCatcher", sbn.packageName+" is read")
        }
    }

    fun getNotification(id: String) : StatusBarNotification {
        return getActiveNotifications(arrayOf(id))[0]
    }
}