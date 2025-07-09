package com.example.notificationmanager

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notificationmanager.db.entities.RuleInfo
import com.example.notificationmanager.ui.compose.Collapsable
import com.example.notificationmanager.ui.compose.Utility


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

@SuppressLint("UnrememberedMutableState")
@Composable
fun RulesList(viewModel: RuleViewModel) {
    val rules by viewModel.ruleInfos.collectAsState(initial = emptyList())

    var visible = remember {
        MutableList(rules.size) { mutableStateOf(false) }
    }


    Log.d("RulesActivity", "Before lazy column indexed")
    LazyColumn() {
        itemsIndexed(rules) { idx, ruleInfo ->
            if (idx >= visible.size) {
                visible.add(mutableStateOf(false))
            }
            Collapsable(
                condition = visible[idx],
            ) {
                Text(
                    text = ruleInfo.rule.name,
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }

            if (visible[idx].value) {
                DisplayRule(viewModel, ruleInfo)
            }
        }
    }
}

@Composable
fun DisplayRule(viewModel: RuleViewModel, ruleInfo: RuleInfo) {
    viewModel.setDao(ruleInfo)
    val actionTypes by ruleInfo.actionTypes.collectAsState(initial = emptyList())

    Log.d("RulesActivity", "Before lazy column non indexed")
    FlowRow() {
        for (actionType in actionTypes) {
            Text(
                text = actionType.getDisplayName(),
                color = colorResource(R.color.white),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun RulesScreenBody(navController: NavController, viewModel: RuleViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
            modifier=modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            YourRulesText()
            RulesList(viewModel)
        }
        AddRuleFAB(
            navController=navController,
            modifier=Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun AddRuleFAB(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    SmallFloatingActionButton(
        onClick = {
            navController.navigate("rules/edit")
//            context.startActivity(Intent(context, CreateRuleActivity::class.java))
        },
        containerColor = Color.Transparent,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(0.dp), // No shadow
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.add_rule_icon),
            contentDescription = "Add Rule Button",
            modifier = Modifier.size(64.dp)
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun RulesScreen(navController: NavController) {
    val applicationContext = LocalContext.current.applicationContext as Application

    val viewModel: RuleViewModel = viewModel(
        factory = ApplicationViewModelFactory(applicationContext)
    )

    Column (
        modifier=Modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
    ) {
        Utility().PercentEmpty(0.1f)
        RulesScreenBody(navController, viewModel, modifier=Modifier.weight(1f))
//        Utility().BottomBar("ComposeRulesActivity")
    }
}