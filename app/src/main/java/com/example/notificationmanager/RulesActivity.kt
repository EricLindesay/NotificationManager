package com.example.notificationmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.notificationmanager.ui.compose.Collapsable
import com.example.notificationmanager.ui.compose.Utility
import com.example.notificationmanager.ui.theme.MyApplicationTheme

class RulesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val viewModel: NotificationViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        setContent {
            MyApplicationTheme {
                RulesScreen()
            }
        }
    }
}

@Composable
fun YourRulesText() {
    Row() {
        Text(
            "Your Rules",
            color = colorResource(R.color.white),
            textDecoration = TextDecoration.Underline,
            fontSize = 22.sp,
            modifier = Modifier.weight(1f),
        )
        Icon(
            painter = painterResource(R.drawable.filter_icon),
            contentDescription = "",
            tint = colorResource(R.color.white),
            modifier = Modifier
                .size(32.dp)
        )
    }
}

@Composable
fun RulesList() {
    // have a list of show rule or not
    // have a list of rules data
//    val rules: List<Rules>
//    var visible = remember { mutableStateOf( listOf(true, false, false, false, true)) }
    var visible = remember { listOf(mutableStateOf(true),
                                    mutableStateOf(false),
                                    mutableStateOf(false),
                                    mutableStateOf(false),
                                    mutableStateOf(true),
    ) }
    val rules: List<Int> = listOf(1, 2, 3, 4, 5)
    LazyColumn() {
        itemsIndexed(rules) { idx, rule ->
            Collapsable(
                condition = visible[idx],
            ) {
                Text("Rule $rule")
            }
        }
    }
}

@Composable
fun RulesScreenBody(modifier: Modifier = Modifier) {
    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        modifier=modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        YourRulesText()
        RulesList()
    }
}

@Preview(showBackground = true)
@Composable
fun RulesScreen() {
    Column (
        modifier=Modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
    ) {
        Utility().PercentEmpty(0.1f)
        RulesScreenBody(modifier=Modifier.weight(1f))
        Utility().BottomBar("ComposeMainActivity")
    }
}