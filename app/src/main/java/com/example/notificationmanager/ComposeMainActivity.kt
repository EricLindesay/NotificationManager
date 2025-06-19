package com.example.notificationmanager

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notificationmanager.ui.theme.MyApplicationTheme
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.notificationmanager.db.NotificationInfo
import com.example.notificationmanager.ui.compose.Utility


class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val viewModel: NotificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        setContent {
            MyApplicationTheme {
                Screen(viewModel)
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}


@Composable
fun NotificationRecycler(viewModel: NotificationViewModel, modifier: Modifier = Modifier) {
    val active by viewModel.activeNotificationsUnread.collectAsState(initial = emptyList())
    val silent by viewModel.silentNotificationsUnread.collectAsState(initial = emptyList())

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
//            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp)  // start, top, end, bottom

    ) {
        item {
            Text(
                "You have ${active.size} new notifications",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        items(active) { notification: NotificationInfo ->
//            ActiveNotificationCard(notification)
//            Text(text="Title: ${notification.title}")
            NotificationCard(notification)
        }

        item {
            Text(
                "You have ${silent.size} silent notifications",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

        items(silent) { notification: NotificationInfo ->
//            Text(text="Title: ${notification.title}")
            NotificationCard(notification)
//            SilentNotificationCard(notification)
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationInfo, modifier: Modifier = Modifier) {
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
            Text(text=notification.title, color=Color(textColor))
        }

        Text(
            text=notification.text,
            color=Color(textColor),
        )
    }
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 48
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 48

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

@Composable
fun Screen(viewModel: NotificationViewModel, modifier: Modifier = Modifier) {
//    var active: ArrayList<String> = arrayListOf("a", "b");
//    var silent: ArrayList<String> = arrayListOf("c", "d");
    Column (
        modifier=modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
    ) {
        Utility().PercentEmpty(0.1f)
        NotificationRecycler(viewModel, Modifier.weight(1f))
        Utility().BottomBar("ComposeMainActivity")
    }
}

fun getTextColorForBackground(backgroundColor: Int): Int {
    // Extract RGB components
    val red = (android.graphics.Color.red(backgroundColor) / 255.0).toFloat()
    val green = (android.graphics.Color.green(backgroundColor) / 255.0).toFloat()
    val blue = (android.graphics.Color.blue(backgroundColor) / 255.0).toFloat()

    // Calculate luminance
    val luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue

    // Return black for bright background, white for dark background
    return if (luminance > 0.5) android.graphics.Color.BLACK else android.graphics.Color.WHITE
}


//@Preview(showBackground = true)
//@Composable
//fun RecyclerPreview() {
//    MyApplicationTheme {
//        Screen()
//    }
//}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
//    val context = LocalContext.current
//    val viewModel: NotificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
    MyApplicationTheme {
//        Screen(viewModel)
//        NotificationCard(info)
    }
}
