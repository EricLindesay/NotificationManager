package com.example.notificationmanager

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModelProvider
import com.example.notificationmanager.db.DBUtility
import com.example.notificationmanager.db.NotificationInfo
import com.example.notificationmanager.db.PackageTypeCount
import com.example.notificationmanager.ui.compose.Utility
import com.example.notificationmanager.ui.theme.MyApplicationTheme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.random.Random

class DataActivity : ComponentActivity() {
    private val ENABLED_NOTIFICATION_LISTENERS: String = "enabled_notification_listeners"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS: String =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    companion object {
        val PACKAGE: String = "com.example.notificationmanager"
        val NEW_NOTIF: String = ".new_notif"
        val SETUP: String = ".setup"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val viewModel: NotificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        setContent {
            MyApplicationTheme {
                Screen2(viewModel)
            }
        }
//
//        val setupBroadcastReceiver = SetupBroadcastReceiver()
//        val intentFilter2 = IntentFilter()
//        intentFilter2.addAction(PACKAGE + SETUP)
//        registerReceiver(setupBroadcastReceiver, intentFilter2, RECEIVER_EXPORTED)
//

        if (!isNotificationServiceEnabled()) {
            val enableNotificationListenerAlertDialog: android.app.AlertDialog? = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog!!.show()
        }
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

    inner class SetupBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedNotifications: ArrayList<NotificationInfo?>? = intent.getSerializableExtra("Notification Code") as? ArrayList<NotificationInfo?>
            Log.d("Test", "Received notifications")
            if (receivedNotifications == null) {
                return
            }

        }
    }
}

@Composable
fun BarChartComposable(data: List<List<Int>>, labels: List<String>, modifier: Modifier = Modifier) {
    val lContext = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                legend.isEnabled = false
                setDrawGridBackground(false)
                setVisibleXRangeMaximum(7f)
                setVisibleXRangeMinimum(7f)
                setTouchEnabled(true)
                setFitBars(true) // Makes sure bars fit nicely
                isDragEnabled = true
                setExtraOffsets(0f, 0f, 0f, 16f) // left, top, right, bottom
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, values ->
                BarEntry(index.toFloat(), values.map { it.toFloat() }.toFloatArray())
            }

            val dataSet = BarDataSet(entries, "Data").apply {
                color = getColor(R.color.blue_active)
                valueTextColor = getColor(R.color.black)
                valueTextSize = 12f
                setDrawValues(false)

                setColors(
                    getColor(lContext, R.color.blocked),   // Blocked
                    getColor(lContext, R.color.silenced),  // Silenced
                    getColor(lContext, R.color.allowed)    // Allowed
                )
                stackLabels = arrayOf("Allowed", "Silenced", "Blocked")

            }

            val xAxis: XAxis = chart.xAxis

            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return labels[index % labels.size]
                }
            }

            // X-Axis Data
            xAxis.setAxisMinimum(-0.5f)
            xAxis.setAxisMaximum(6.5f)

            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.textColor = getColor(lContext, R.color.white)
            xAxis.textSize = 16f


            // Y-Axis Data
            val leftAxis: YAxis = chart.axisLeft
            setYAxis(leftAxis, lContext)

            val rightAxis: YAxis = chart.axisRight
            setYAxis(rightAxis, lContext)

            chart.data = BarData(dataSet)
//            chart.animateY(2000)
            chart.invalidate()
        }
    )
}

fun setYAxis(axis: YAxis, context: Context) {
    axis.setDrawGridLines(true)
    axis.gridColor = getColor(context, R.color.light_gray)
    axis.gridLineWidth = 1f
    axis.textColor = getColor(context, R.color.white)
    axis.axisMinimum = 0f
    axis.setDrawZeroLine(true)
    axis.textSize = 14f
    axis.isEnabled = true
}

@Composable
fun DropDownMenu() {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val usernames = listOf("All Notifications", "Allowed", "Silenced", "Blocked")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box (
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(R.color.blue_inactive))
                .padding(4.dp, 4.dp, 0.dp, 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(
                    text = usernames[itemPosition.value],
                    fontSize = 20.sp,
                    color = colorResource(R.color.white),
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "DropDown Icon",
                    colorFilter = ColorFilter.tint(Color.White)
//                    modifier = Modifier
//                        .background(colorResource(R.color.white))
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                modifier = Modifier
                    .background(Color.White),
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                usernames.forEachIndexed { index, username ->
                    DropdownMenuItem(text = {
                        Text(
                            text = username,
                            color = colorResource(R.color.black),
                        )
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                        })
                }
            }
        }

    }
}

@Composable
fun spToDp(size: TextUnit) : Dp {
    return with(LocalDensity.current) { size.toDp() }
}

@Composable
fun ColouredCircle(color: Color, size: TextUnit) {
    val sizeDp = spToDp(size)

    Box(
        modifier = Modifier
            .size(sizeDp)
//            .fillMaxHeight()
//            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun KeyComposable() {
    val fontSize = 18.sp
    val fontColor = colorResource(R.color.white)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
//            .height(22.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
//                .padding(end=4.dp)
        ) {
            ColouredCircle(colorResource(R.color.allowed), fontSize)
            Text(
                "Allowed",
                fontSize = fontSize,
                color = fontColor,
                modifier = Modifier
//                    .weight(1f)
                    .padding(start=4.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
//                .padding(start=2.dp, end=2.dp)
        ) {
            ColouredCircle(colorResource(R.color.silenced), fontSize)
            Text(
                "Silenced",
                fontSize = fontSize,
                color = fontColor,
                modifier = Modifier
//                    .weight(1f)
                    .padding(start=4.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
//                .padding(end=4.dp)
        ) {
            ColouredCircle(colorResource(R.color.blocked), fontSize)
            Text(
                "Blocked",
                fontSize = fontSize,
                color = fontColor,
//                textAlign = TextAlign.Center,
                modifier = Modifier
//                    .weight(1f)
                    .padding(start=4.dp)
            )
        }
    }
}

fun generateRandomLists(): List<List<Float>> {
    return List(7) {
        List(3) {
            Random.nextFloat()*100 // random number between 0 and 99
        }
    }
}

@Composable
fun DateNavigator(modifier: Modifier = Modifier) {
    val fontSize = 20.sp
    val chevronSize = spToDp(fontSize*1.1)
    Row(
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painterResource(R.drawable.chevron_left),
            contentDescription = "Left Date Navigator",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(chevronSize)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        println("Decrease date")
                    },
                )
        )
        Text(
            "Mon, 14 Dec",
            fontSize = fontSize,
            color = colorResource(R.color.white),
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Image(
            painterResource(R.drawable.chevron_right),
            contentDescription = "Right Date Navigator",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(chevronSize)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        println("Increase date")
                    },
                )
        )
    }
}

@Composable
fun NumEachNotification(info: PackageTypeCount?, modifier: Modifier = Modifier) {
    val fontSize: TextUnit = 16.sp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        var numAllowed: Int = 0
        var numSilenced: Int = 0
        var numBlocked: Int = 0
        info?.apply {
            numAllowed = this.allowedCount
            numSilenced = this.silentCount
            numBlocked = this.blockedCount
        }
        Text(
            "$numAllowed Allowed",
            color = colorResource(R.color.allowed),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = fontSize,
        )
        Text(
            "$numSilenced Silenced",
            color = colorResource(R.color.silenced),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = fontSize,
        )
        Text(
            "$numBlocked Blocked",
            color = colorResource(R.color.blocked),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = fontSize,
        )
    }
}

@Composable
fun ChartSection(viewModel: NotificationViewModel) {
    // This would be whatever date the user currently has input
    val focusedTime : String = Utility().millisToDateTimeString(System.currentTimeMillis())
    val chartData: List<List<Int>> by viewModel.getPastNDayInfoAsInts(focusedTime, 7).collectAsState(initial = emptyList())
    val dailyInfo: PackageTypeCount? by viewModel.todayNotificationCounts.collectAsState(initial = null)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var total = 0
        dailyInfo?.let {
            total = it.total
        }
        Text(
            text = "$total Notifications",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
        )
        Text(
            text = "Today",
            fontSize = 24.sp,
            color = colorResource(R.color.blue_active),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        DropDownMenu()

        KeyComposable()

        val labels = listOf( "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" )
        BarChartComposable(
            chartData,
            labels,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        DateNavigator()
        NumEachNotification(
//            info = null,
            info = dailyInfo,
            modifier = Modifier
                .padding(top=8.dp)
        )
    }
}

@Composable
//fun NotificationList(notifications: List<NotificationInfo>, modifier: Modifier = Modifier) {
fun NotificationList(viewModel: NotificationViewModel, modifier: Modifier = Modifier) {
    // Will probably want to filter by day as well
    val pkgs by viewModel.todayNotificationCountsPerPackage.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val util = DBUtility()

//    val testInfo by viewModel.testClass.collectAsState(initial = emptyList())
//    for (t in testInfo) {
//        Log.d("TEST", "Test info: "+t.day +" has "+t.count);
//    }


    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
//            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp)  // start, top, end, bottom

    ) {
        items(pkgs) { pkg: PackageTypeCount ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom=8.dp)
            ) {
                // get package icon
                util.getPackageIcon(context, pkg.identifier).let { drawable ->
                    val painter = remember(drawable) {
                        val bitmap = drawableToBitmap(drawable)
                        BitmapPainter(bitmap.asImageBitmap())
                    }

                    Image(
                        painter = painter,
                        contentDescription = "Notification Icon",
                        modifier = Modifier
                            .size(56.dp)
                            .padding(end = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    // App name
                    Text(
                        util.getAppName(context, pkg.identifier),
//                        pkg.packageName,
                        fontSize = 20.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                    )
                    // %d Total
                    Text(
                        "${pkg.total} Total",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "${pkg.allowedCount} Allowed",
                        color = colorResource(R.color.allowed),
                        fontSize = 16.sp,
                    )
                    Text(
                        "${pkg.silentCount} Silenced",
                        color = colorResource(R.color.silenced),
                        fontSize = 16.sp,
                    )
                    Text(
                        "${pkg.blockedCount} Blocked",
                        color = colorResource(R.color.blocked),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun Screen2(viewModel: NotificationViewModel) {
    val context = LocalContext.current
    val n1 = NotificationInfo("com.example.main", "Test", "This is the description text")
    ContextCompat.getDrawable(context, R.drawable.ic_home_black_24dp)?.let { n1.setIcon(it) }
//    val n2 = NotificationInfo("com.example.example.example.example.main", "Test", "This is the description text")
//    ContextCompat.getDrawable(context, R.drawable.ic_home_black_24dp)?.let { n2.setIcon(it) }

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
            Utility().DataNavigationButtons("Activity")
            ChartSection(viewModel)
            NotificationList(viewModel)
//            NotificationList(listOf(n1,n1,n1,n1,n1,n1,n1,n1,n1,n1,n1,n1,n1,n1,n1,n1))
        }
        Utility().BottomBar("ComposeDataActivity")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComposable() {
    MyApplicationTheme() {
//        Screen2()
    }
}