package com.example.notificationmanager.ui.compose

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notificationmanager.ComposeDataActivity
import com.example.notificationmanager.ComposeHistoryActivity
import com.example.notificationmanager.ComposeMainActivity
import com.example.notificationmanager.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Utility {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())

    fun millisToDateTimeString(time: Long) : String {
        return dateTimeFormatter.format(Instant.ofEpochMilli(time))
    }

    fun millisToDateString(time: Long) : String {
        return dateFormatter.format(Instant.ofEpochMilli(time))
    }

    @Composable
    fun BottomBar(currentScreen: String, modifier: Modifier = Modifier) {
        val context = LocalContext.current
        Column (
            modifier=modifier
                .fillMaxWidth()
                .background(colorResource(R.color.purple_500))
                .padding(16.dp, 4.dp, 16.dp, 0.dp)   // start, top, end, bottom
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        // Switch to data activity
                        if (currentScreen != "ComposeDataActivity") {
                            context.startActivity(Intent(context, ComposeDataActivity::class.java))
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.DataThresholding,
                        contentDescription = "Activity Icon",
                        modifier = Modifier
                            .size(64.dp)
                    )
                }
                IconButton(
                    onClick = {
                        // Switch to home/default activity
                        if (currentScreen != "ComposeMainActivity") {
                            context.startActivity(Intent(context, ComposeMainActivity::class.java))
                        }                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home Icon",
                        modifier = Modifier
                            .size(64.dp)
                            .weight(1f)
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = "Unused Icon",
                        modifier = Modifier
                            .size(64.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    fun PercentEmpty(percent: Float) {
        BoxWithConstraints(
            modifier = Modifier
        ) {
            val parentHeight = maxHeight
            val verticalOffset = (parentHeight * percent) // 10% of parent height

            Box(
                modifier = Modifier
                    .size(verticalOffset)
            )
        }
    }

    @Composable
    fun DataNavigationButtons(activeScreen: String, modifier: Modifier = Modifier) {
        val context = LocalContext.current

        val activeColor = colorResource(R.color.blue_active)
        val inactiveColor = colorResource(R.color.blue_inactive)
        val activeTextColor = colorResource(R.color.black)
        val inactiveTextColor = colorResource(R.color.white)
        Row(
            modifier=modifier
        ) {
            Button(
                onClick = {
                    if (activeScreen != "Activity") {
                        context.startActivity(Intent(context, ComposeDataActivity::class.java))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor =  if (activeScreen == "Activity") activeColor else inactiveColor),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp, 0.dp)
            ) {
                Text(
                    text = "Activity",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (activeScreen == "Activity") activeTextColor else inactiveTextColor,
                )
            }
            Button(
                onClick = {
                    if (activeScreen != "History") {
                        context.startActivity(Intent(context, ComposeHistoryActivity::class.java))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (activeScreen == "History") activeColor else inactiveColor),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp, 0.dp)
            ) {
                Text(
                    "History",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activeScreen == "History") activeTextColor else inactiveTextColor,
                )
            }
            Button(
                onClick = {
                    if (activeScreen != "Blocked") {
                        // Consider having authentication to continue
                        context.startActivity(Intent(context, ComposeBlockedActivity::class.java))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (activeScreen == "Blocked") activeColor else inactiveColor),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp, 0.dp)
            ) {
                Text(
                    "Blocked",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activeScreen == "Blocked") activeTextColor else inactiveTextColor,
                )
            }
        }
    }

}