package com.example.notificationmanager

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notificationmanager.ui.theme.MyApplicationTheme


@Composable
fun BlockedScreen(navController: NavController) {
    val applicationContext = LocalContext.current.applicationContext as Application

    val viewModel: NotificationViewModel = viewModel(
        factory = ApplicationViewModelFactory(applicationContext)
    )

    val notifications by viewModel.blockedNotifications.collectAsState(initial = emptyList())
    NotificationListScreen(navController, notifications)
}
