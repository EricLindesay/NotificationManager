package com.example.notificationmanager

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification


class NotificationListenerExampleService : NotificationListenerService() {
    /*
        These are the package names of the apps. for which we want to
        listen the notifications
     */

    /*
        These are the return codes we use in the method which intercepts
        the notifications, to decide whether we should do something or not
     */

    override fun onBind(intent: Intent?): IBinder? {
        println("In on bind")
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
//        int notificationCode = matchNotificationCode(sbn);

//        if(notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE){

        println("Pre send 1")
        val intent = Intent(MainActivity.PACKAGE)
        println(sbn.packageName)
        intent.putExtra("PackageName", sbn.packageName)
        sendBroadcast(intent)
        println("Sent success")
//        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
//        int notificationCode = matchNotificationCode(sbn);

//        if(notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {

        val activeNotifications = this.activeNotifications

        if (activeNotifications != null && activeNotifications.size > 0) {
            for (i in activeNotifications.indices) {
//                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                val intent = Intent("com.example.notificationmanager")
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