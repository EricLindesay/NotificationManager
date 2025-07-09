package com.example.notificationmanager

import android.app.Application
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.notificationmanager.db.DatabaseBuilder
import com.example.notificationmanager.db.entities.NotificationInfo
import com.example.notificationmanager.ui.compose.BottomNavBar
import com.example.notificationmanager.ui.compose.Collapsable
import com.example.notificationmanager.ui.compose.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()


        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseBuilder.getInstance(application.applicationContext)
//            db.ruleDao().insertDummyRuleWithTargets()
//            db.ruleDao().insertAdCapTarget()
        }
//
        setContent {
            MyApplicationTheme {
                App()
            }
        }
    }
}


@Composable
fun NotificationRecycler(viewModel: NotificationViewModel, modifier: Modifier = Modifier) {
    val active by viewModel.activeNotificationsUnread.collectAsState(initial = emptyList())
    val silent by viewModel.silentNotificationsUnread.collectAsState(initial = emptyList())
    var showActive = remember { mutableStateOf(true) }
    var showSilent = remember { mutableStateOf(false) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
//            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp)  // start, top, end, bottom
    ) {
        item {
            ActiveNotificationCountText("new", active.size, showActive)
        }

        if (showActive.value) {
            items(active) { notification: NotificationInfo ->
                NotificationCard(notification)
            }
        }

        item {
            ActiveNotificationCountText("silent", silent.size, showSilent)
        }

        if (showSilent.value) {
            items(silent) { notification: NotificationInfo ->
                NotificationCard(notification)
            }
        }
    }
}

@Composable
fun ActiveNotificationCountText(type: String, numNotifications: Int, showActive: MutableState<Boolean>) {
    Collapsable(
        condition = showActive,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            "You have $numNotifications $type notifications",
            color = colorResource(R.color.white),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
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
fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
    val applicationContext = LocalContext.current.applicationContext as Application

    val viewModel: NotificationViewModel = viewModel(
        factory = ApplicationViewModelFactory(applicationContext)
    )

    Column (
        modifier=modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
    ) {
        Utility().PercentEmpty(0.1f)
        NotificationRecycler(viewModel, Modifier.weight(1f))
//        Utility().BottomBar("ComposeMainActivity")
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

@Composable
fun getEditViewModelForRule(id: Long): EditRuleViewModel {
    val applicationContext = LocalContext.current.applicationContext as Application

    val viewModel: RuleViewModel = viewModel(
        factory = ApplicationViewModelFactory(applicationContext)
    )

    val editRuleViewModel: EditRuleViewModel = viewModel(
        factory = ApplicationViewModelFactory(applicationContext)
    )

    val ruleInfo = viewModel.getRuleInfo(id=id).collectAsState(initial=null)
    editRuleViewModel.setRuleState(ruleInfo)
    return editRuleViewModel
}

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
                    },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") { MainScreen(navController) }

            navigation(startDestination = "data/info", route = "data") {
                composable("data/info") { DataScreen(navController) }
                composable("data/history") { HistoryScreen(navController) }
                composable("data/blocked") { BlockedScreen(navController) }
            }

            navigation(startDestination = "rules/list", route = "rules") {
                composable("rules/list") {
                    RulesScreen(navController)
                }
                composable("rules/edit") {
                    val editRuleViewModel = getEditViewModelForRule(id=2)
                    CreateRuleScreen(navController, editRuleViewModel)
                }
                composable("rules/edit/targets") {
                    val editRuleViewModel = getEditViewModelForRule(id=2)
                    RuleTargetListScreen(navController, editRuleViewModel)
                }
            }
        }
    }
}