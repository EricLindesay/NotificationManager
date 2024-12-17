package com.example.notificationmanager

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationmanager.ExampleMain.ImageChangeBroadcastReceiver
import com.example.notificationmanager.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val ENABLED_NOTIFICATION_LISTENERS: String = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS: String =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    companion object {
        val PACKAGE: String = "com.example.notificationmanager"
        val NEW_NOTIF: String = ".new_notif"
        val SETUP: String = ".setup"
    }
    private lateinit var binding: ActivityMainBinding
    private var imageChangeBroadcastReceiver: ImageChangeBroadcastReceiver? = null
    private var setupBroadcastReceiver: SetupBroadcastReceiver? = null
    private var textView: TextView? = null

    private var enableNotificationListenerAlertDialog: android.app.AlertDialog? = null
    private var customAdapter: CustomAdapter? = null
    private var data: ArrayList<NotificationInfo?> = ArrayList()
//    private var data: ArrayList<String?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
//        val dataset = arrayOf("January", "February", "March")
        customAdapter = CustomAdapter(data)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter

        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog!!.show()
        }
        textView = this.findViewById<TextView>(R.id.textView)

        imageChangeBroadcastReceiver = ImageChangeBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(PACKAGE + NEW_NOTIF)
        registerReceiver(imageChangeBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)

        setupBroadcastReceiver = SetupBroadcastReceiver()
        val intentFilter2 = IntentFilter()
        intentFilter2.addAction(PACKAGE + SETUP)
        registerReceiver(setupBroadcastReceiver, intentFilter2, RECEIVER_EXPORTED)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    inner class SetupBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedNotificationCode: ArrayList<NotificationInfo?>? = intent.getSerializableExtra("Notification Code") as? ArrayList<NotificationInfo?>
            println("Received setup")
            if (receivedNotificationCode != null) {
                for (n: NotificationInfo? in receivedNotificationCode) {
                    if (!(n in data)) {
                        data.add(n)
                        // Add the icon
                        if (n!!.packageName != null) {
                            println("Getting packagemanager")
                            val packageManager = context.packageManager
//                            val applicationInfo = packageManager.getApplicationIcon(n.packageName!!)
                            println("Getting icon")
                            val appIcon = packageManager.getApplicationIcon(n.packageName!!)
                            println("setting icon")
                            n.icon = appIcon
                        }
                    }
                }
//                data = receivedNotificationCode
//                println(data)
                println("Update adapter")
                customAdapter?.notifyItemInserted(data.size-1)
//                println(receivedNotificationCode)
            }
//                changeInterceptedNotificationImage(receivedNotificationCode)
        }
    }

    private fun changeInterceptedNotificationImage(notificationCode: String) {
        textView!!.text = notificationCode
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
}