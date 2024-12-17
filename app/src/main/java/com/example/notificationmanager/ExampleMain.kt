package com.example.notificationmanager

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ExampleMain : AppCompatActivity() {
    private val ENABLED_NOTIFICATION_LISTENERS: String = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS: String =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    private val interceptedNotificationImageView: ImageView? = null
    private var imageChangeBroadcastReceiver: ImageChangeBroadcastReceiver? = null
    private var enableNotificationListenerAlertDialog: android.app.AlertDialog? = null
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_main)

        // Here we get a reference to the image we will modify when a notification is received
//        interceptedNotificationImageView = findViewById<View>(R.id.intercepted_notification_logo) as ImageView
        textView = this.findViewById(R.id.textView)
        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog!!.show()
        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received
        imageChangeBroadcastReceiver = ImageChangeBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.github.chagall.notificationlistenerexample")
        registerReceiver(imageChangeBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(imageChangeBroadcastReceiver)
    }

    /**
     * Change Intercepted Notification Image
     * Changes the MainActivity image based on which notification was intercepted
     * @param notificationCode The intercepted notification code
     */
    private fun changeInterceptedNotificationImage(notificationCode: String) {
        textView!!.text = notificationCode
//        switch(notificationCode){
//            case NotificationListenerExampleService.InterceptedNotificationCode.FACEBOOK_CODE:
//                interceptedNotificationImageView.setImageResource(R.drawable.facebook_logo);
//                break;
//            case NotificationListenerExampleService.InterceptedNotificationCode.INSTAGRAM_CODE:
//                interceptedNotificationImageView.setImageResource(R.drawable.instagram_logo);
//                break;
//            case NotificationListenerExampleService.InterceptedNotificationCode.WHATSAPP_CODE:
//                interceptedNotificationImageView.setImageResource(R.drawable.whatsapp_logo);
//                break;
//            case NotificationListenerExampleService.InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE:
//                interceptedNotificationImageView.setImageResource(R.drawable.other_notification_logo);
//                break;
//        }
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Image Change Broadcast Receiver.
     * We use this Broadcast Receiver to notify the Main Activity when
     * a new notification has arrived, so it can properly change the
     * notification image
     */
    inner class ImageChangeBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedNotificationCode: String? = intent.getStringExtra("Notification Code")
            if (receivedNotificationCode != null)
                changeInterceptedNotificationImage(receivedNotificationCode)
        }
    }


    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private fun buildNotificationServiceAlertDialog(): android.app.AlertDialog {
        val alertDialogBuilder = android.app.AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(
            R.string.yes
        ) { dialog, id -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        alertDialogBuilder.setNegativeButton(
            R.string.no
        ) { dialog, id ->
            // If you choose to not enable the notification listener
            // the app. will not work as expected
        }
        return (alertDialogBuilder.create())
    }
//    private val ENABLED_NOTIFICATION_LISTENERS: String = "enabled_notification_listeners"
//    private val ACTION_NOTIFICATION_LISTENER_SETTINGS: String =
//        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
//
//    private val interceptedNotificationImageView: TextView? = null
//    public var title: TextView? = null
//    private var imageChangeBroadcastReceiver: ImageChangeBroadcastReceiver? = null
//    private var enableNotificationListenerAlertDialog: AlertDialog? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(com.example.notificationmanager.R.layout.example_main)
//
//        // Here we get a reference to the image we will modify when a notification is received
////        var interceptedNotificationImageView = this.findViewById(R.id.intercepted_notification_logo) as ImageView?
//        title = this.findViewById(com.example.notificationmanager.R.id.textView)
//
//        // If the user did not turn the notification listener service on we prompt him to do so
//        if (!isNotificationServiceEnabled()) {
//            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
//            enableNotificationListenerAlertDialog!!.show()
//        }
//
//        // Finally we register a receiver to tell the MainActivity when a notification has been received
//        imageChangeBroadcastReceiver = ImageChangeBroadcastReceiver()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("com.github.chagall.notificationlistenerexample")
//        registerReceiver(imageChangeBroadcastReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
//    }
//
//    protected override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(imageChangeBroadcastReceiver)
//    }
//
//    /**
//     * Change Intercepted Notification Image
//     * Changes the MainActivity image based on which notification was intercepted
//     * @param notificationCode The intercepted notification code
//     */
//    private fun changeInterceptedNotificationImage(notificationTitle: String) {
//        title?.setText(notificationTitle)
//    }
//
//    /**
//     * Is Notification Service Enabled.
//     * Verifies if the notification listener service is enabled.
//     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
//     * @return True if enabled, false otherwise.
//     */
//    private fun isNotificationServiceEnabled(): Boolean {
//        val pkgName: String = getPackageName()
//        val flat: String = Settings.Secure.getString(
//            getContentResolver(),
//            ENABLED_NOTIFICATION_LISTENERS
//        )
//        if (!TextUtils.isEmpty(flat)) {
//            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            for (i in names.indices) {
//                val cn = ComponentName.unflattenFromString(names[i])
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.packageName)) {
//                        return true
//                    }
//                }
//            }
//        }
//        return false
//    }
//
//    /**
//     * Image Change Broadcast Receiver.
//     * We use this Broadcast Receiver to notify the Main Activity when
//     * a new notification has arrived, so it can properly change the
//     * notification image
//     */
//    inner class ImageChangeBroadcastReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val receivedNotificationCode = intent.getStringExtra("Notification Code")
//            println("Got a notification " +receivedNotificationCode)
//            if (receivedNotificationCode != null)
//                changeInterceptedNotificationImage(receivedNotificationCode)
////            title.setText("oisfiojdfasjiodfs");
//        }
//    }
//
//
//    /**
//     * Build Notification Listener Alert Dialog.
//     * Builds the alert dialog that pops up if the user has not turned
//     * the Notification Listener Service on yet.
//     * @return An alert dialog which leads to the notification enabling screen
//     */
//    private fun buildNotificationServiceAlertDialog(): AlertDialog {
//        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@ExampleMain)
////        alertDialogBuilder.setTitle(R.string.notification_listener_service)
////        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
//        alertDialogBuilder.setPositiveButton(R.string.yes,
//            DialogInterface.OnClickListener { dialog, id ->
//                startActivity(
//                    Intent(
//                        ACTION_NOTIFICATION_LISTENER_SETTINGS
//                    )
//                )
//            })
//        alertDialogBuilder.setNegativeButton(R.string.no,
//            DialogInterface.OnClickListener { dialog, id ->
//                // If you choose to not enable the notification listener
//                // the app. will not work as expected
//            })
//        return (alertDialogBuilder.create())
//    }

}