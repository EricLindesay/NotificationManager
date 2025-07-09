package com.example.notificationmanager

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.notificationmanager.db.entities.RuleInfo
import com.example.notificationmanager.db.entities.RuleTargetInfo


@Composable
fun RuleLoading(modifier: Modifier = Modifier) {
    Text(
        text = "Loading...",
        color = colorResource(R.color.white),
        fontSize = 22.sp,
        textDecoration = TextDecoration.Underline,
        modifier = modifier,
    )
}

fun getTargets(context: Context, selected: List<RuleTargetInfo>, appTargetList: List<RuleTargetInfo>, isAllSelected: Boolean) : List<RuleTargetInfo> {
//    val isAllSelected = remember { ruleInfo.hasTargetAll() }.collectAsState(initial=false)
    val allAppsTarget = RuleTargetInfo(
        context,
        "All Apps",
        "All Apps",
        ContextCompat.getDrawable(context, R.drawable.all_inclusive_icon),
        isAllSelected
    )

    var ans: List<RuleTargetInfo> =
        (selected+appTargetList).distinctBy { it.packageName }.sortedBy { it.displayName.lowercase() }

    ans = listOf(allAppsTarget) + ans
    return ans;
}

@Composable
fun RuleTargetListBody(navController: NavController, ruleInfo: RuleInfo, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Reactively observe state
    val isAllSelected by ruleInfo.hasTargetAll().collectAsState(initial = false)
    val selected by ruleInfo.getRuleTargetInfoPackages(context).collectAsState(initial = emptyList())
    for (s in selected) {
        Log.d("RuleTargetList", "Name: ${s.displayName}")
    }

    // Cache app list (doesn't change, so no recomposition needed)
    val appTargetList = remember {
        NotificationPermissionHelper(context).getAllAppsTargetInfo()
    }

    // Combine everything into final targets reactively
    val targets = remember(isAllSelected, selected, appTargetList) {
        getTargets(context, appTargetList, selected, isAllSelected)
    }

//    val selectedMap = remember { mutableStateMapOf<String, Boolean>() }

//    val targets: List<RuleTargetInfo> = getTargets(LocalContext.current)

//    val context = LocalContext.current
//    val stateFlow: StateFlow<List<RuleTargetInfo>> = ruleInfo.getRuleTargetInfos(context)
//    val validTargets by stateFlow.collectAsState()

//    val selected;
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(targets, key = { it.packageName }) { ruleTargetInfo ->
            DisplayRuleTargetInfo(ruleTargetInfo)
            HorizontalDivider()
        }
    }
}

@Composable
fun DisplayRuleTargetInfo(ruleTargetInfo: RuleTargetInfo) {
    val checked: MutableState<Boolean> = remember { mutableStateOf(false) }
    checked.value = ruleTargetInfo.selected

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .clickable {
                checked.value = !checked.value
                ruleTargetInfo.selected = !ruleTargetInfo.selected
            }
    ) {
        Image(
            bitmap = drawableToBitmap(ruleTargetInfo.icon).asImageBitmap(),
            contentDescription = "${ruleTargetInfo.displayName} App Icon",
            modifier = Modifier
                .size(24.dp)
        )
        Text(
            text = ruleTargetInfo.displayName,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        )
        Checkbox(
            checked = checked.value,
//            checked = ruleTargetInfo.selected,
            onCheckedChange = {
                checked.value = !checked.value
                ruleTargetInfo.selected = !ruleTargetInfo.selected
            },
        )
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(color= colorResource(R.color.green), shape= RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        TextField(
            value = "test",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
            // singleline
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = colorResource(R.color.white),
            modifier = Modifier
//                .fillMaxSize(0.1f)
                .fillMaxHeight()
                .aspectRatio(1f)
        )
    }
}

@Composable
fun RuleTargetListScreen(navController: NavController, editRuleViewModel: EditRuleViewModel) {
    val ruleInfo = editRuleViewModel.getRuleInfo()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
    ) {
//        Utility().PercentEmpty(0.1f)
        SearchBar()
        if (ruleInfo == null) {
            RuleLoading(Modifier.weight(1f))
        } else {
            RuleTargetListBody(navController, ruleInfo, Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDisplay() {
//    RuleTargetListBody()
}

class NotificationPermissionHelper(private val context: Context) {

    fun getAllApps(): List<String> {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList: List<ResolveInfo> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            packageManager.queryIntentActivities(mainIntent, 0)
        }

        return resolveInfoList.mapNotNull { resolveInfo ->
            resolveInfo.activityInfo.packageName
        }
    }

    fun getAllAppsTargetInfo(): List<RuleTargetInfo> {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfoList: List<ResolveInfo> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            packageManager.queryIntentActivities(mainIntent, 0)
        }

        return resolveInfoList.mapNotNull { resolveInfo ->
            RuleTargetInfo(context, resolveInfo.activityInfo.packageName)// .activityInfo.packageName
        }
    }

//    fun getAppsWithNotificationPermission(): List<ApplicationInfo> {
//        val allApps: List<ApplicationInfo> = getAllApps()
//        return allApps.filter { hasNotificationPermission(it.packageName) }
//    }

    private fun hasNotificationPermission(packageName: String): Boolean {
        // Check if the app is opted into notifications
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = getMode(appOpsManager, packageName)
        Log.d("RuleTargetInfo", "${packageName} has mode ${mode}, looking for ${AppOpsManager.MODE_ALLOWED}")
        return mode == AppOpsManager.MODE_ALLOWED

    }

    private fun getMode(appOpsManager: AppOpsManager, packageName: String): Int {
        return onGetOp()(appOpsManager,
            "android:post_notification",
            android.os.Process.myUid(),
            packageName,
        )
    }

    private fun onGetOp(): (AppOpsManager, String, Int, String) -> Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return AppOpsManager::unsafeCheckOpNoThrow
        }
        return AppOpsManager::checkOpNoThrow
    }
}