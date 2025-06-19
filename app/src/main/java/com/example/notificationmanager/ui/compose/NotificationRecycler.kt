package com.example.notificationmanager.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.notificationmanager.NotificationViewModel
import com.example.notificationmanager.ui.theme.MyApplicationTheme

//@Composable
//fun NotificationRecycler(viewModel: NotificationViewModel) {
//    val active by viewModel.activeNotifications.collectAsState(initial = emptyList())
//    val silent by viewModel.silentNotifications.collectAsState(initial = emptyList())
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF00004A))
//            .padding(8.dp)
//    ) {
//        item {
//            Text(
//                "You have ${active.size} new notifications",
//                color = Color.White,
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//
//        items(active) { notification ->
////            ActiveNotificationCard(notification)
//            Text(text="Title: ${notification}")
//        }
//
//        item {
//            Text(
//                "You have ${silent.size} silent notifications",
//                color = Color.White,
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//
//        items(silent) { notification ->
//            Text(text="Title: ${notification}")
////            SilentNotificationCard(notification)
//        }
//    }
//}


//@Preview(showBackground = true)
//@Composable
//fun RecyclerPreview() {
//    MyApplicationTheme {
//        NotificationRecycler()
//    }
//}