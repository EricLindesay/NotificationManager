package com.example.notificationmanager

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notificationmanager.db.entities.NotificationInfo
import com.example.notificationmanager.db.entities.types
import com.example.notificationmanager.ui.compose.DataNavigationButtons
import com.example.notificationmanager.ui.compose.Utility
import com.example.notificationmanager.ui.theme.MyApplicationTheme


@Composable
fun HistoryScreen(navController: NavController) {
    val applicationContext = LocalContext.current.applicationContext as Application

    val viewModel: NotificationViewModel = viewModel(
        factory = ApplicationViewModelFactory(applicationContext)
    )

    val notifications by viewModel.nonBlockedNotifications.collectAsState(initial = emptyList())
    NotificationListScreen(navController, notifications)
}

@Composable
fun NotificationListAll(notifications: List<NotificationInfo>, modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
//            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
            .padding(16.dp, 0.dp, 16.dp, 16.dp)  // start, top, end, bottom

    ) {
        item {
            Text(
                "${notifications.size} notifications",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        items(notifications) { notification: NotificationInfo ->
//            ActiveNotificationCard(notification)
//            Text(text="Title: ${notification.title}")
            HistoryNotificationCard(notification)
        }
    }
}

@Composable
fun HistoryNotificationCard(notification: NotificationInfo, modifier: Modifier = Modifier) {
    var textColor: Int = getTextColorForBackground(notification.color)
    val context = LocalContext.current
    Column(
        modifier=modifier
            .fillMaxWidth()
            .padding(bottom=12.dp)
            .clip(RoundedCornerShape(10.dp)) // Step 1: Clip to rounded shape
            .background(Color(notification.color))
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
    ) {
        Row (
            verticalAlignment=Alignment.CenterVertically
        ) {
            notification.getIcon(context)?.let { drawable ->
                val painter = remember(drawable) {
                    val bitmap = drawableToBitmap(drawable)
                    BitmapPainter(bitmap.asImageBitmap())
                }

                Image(
                    painter = painter,
                    contentDescription = "Notification Icon",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 8.dp)
                )
            }
            Text(
                text=notification.title,
                color=Color(textColor),
                modifier = Modifier.weight(1f)
            )

            if (notification.type == types.SILENT) {
                // Add a silenced icon
                Icon(
                    painter = painterResource(R.drawable.silence_icon),
                    contentDescription = "",
                    tint = Color(textColor),
                )
            }
        }

        Text(
            text=notification.text,
            color=Color(textColor),
        )
    }
}


@Composable
fun NotificationListScreen(navController: NavController, notifications: List<NotificationInfo>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
//            .padding(16.dp, 0.dp, 16.dp, 0.dp)  // start, top, end, bottom
    ) {
        Column(
            modifier = Modifier
                .background(colorResource(R.color.purple_700))
                .padding(16.dp, 0.dp, 16.dp, 0.dp)  // start, top, end, bottom
                .weight(1f)
        ) {
            Utility().PercentEmpty(0.05f)
            DataNavigationButtons(navController)
//            Utility().DataNavigationButtons(activeScreen)
            NotificationListAll(notifications)
        }
//        Utility().BottomBar("ComposeDataActivity")
    }

}