package com.example.notificationmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.example.notificationmanager.ui.theme.MyApplicationTheme

class BlockedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val viewModel: NotificationViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        setContent {
            MyApplicationTheme {
                BlockedScreenIntermediate(viewModel)
            }
        }
    }
}

@Composable
fun BlockedScreenIntermediate(viewModel: NotificationViewModel) {
    val notifications by viewModel.blockedNotifications.collectAsState(initial = emptyList())
    NotificationListScreen(notifications, "Blocked")
}
