package com.example.notificationmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notificationmanager.db.entities.RuleInfo
import com.example.notificationmanager.db.entities.RuleType
import com.example.notificationmanager.ui.compose.Collapsable
import com.example.notificationmanager.ui.compose.Utility

@Composable
fun CreateRuleTextInput(ruleInfo: RuleInfo) {
    val ruleNameState = rememberTextFieldState(ruleInfo.rule.name)

    BasicTextField(
        state = ruleNameState,
        textStyle = TextStyle(
            color = colorResource(R.color.white),
            fontSize = 22.sp,
            textDecoration = TextDecoration.Underline,
        ),
        modifier = Modifier,
        lineLimits = TextFieldLineLimits.SingleLine,
    )
}

@Composable
fun ShowLoadingText() {
    Text(
        text = "Loading...",
        color = colorResource(R.color.white),
        fontSize = 22.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier,
    )
}

@Composable
fun ShowCollapsable(ruleTypes: List<RuleType>, collapseText: String, onClickAdd: () -> Unit) {
    val show = remember { mutableStateOf(true) }

    Collapsable(
        condition = show,
        modifier = Modifier
            .padding(top=16.dp)
    ) {
        Text(
            text = collapseText,
            color = colorResource(R.color.white),
            fontSize = 22.sp,
        )
    }

    if (show.value) {
        ShowTypesList(ruleTypes, collapseText, onClickAdd)
    }
}

@Composable
fun ShowTypesList(ruleTypes: List<RuleType>, addContentDescription: String, onClickAdd: () -> Unit, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        for (ruleType in ruleTypes) {
            DisplayBubble(
                // get the icon for the name
                ruleType.getDisplayName(),
                rightIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove $addContentDescription: ${ruleType.getDisplayName()}",
                        tint = colorResource(R.color.black),
                    )
                }
            )
        }
        DisplayBubble(
            "Add",
            modifier = Modifier
                .clickable {
                    onClickAdd()
                },
            leftIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add $addContentDescription",
                    tint = colorResource(R.color.black),
                )
            }
        )
    }
}

@Composable
fun DisplayBubble(text: String, modifier: Modifier = Modifier, leftIcon: @Composable () -> Unit = {}, rightIcon: @Composable () -> Unit = {}) {
    val shape = RoundedCornerShape(10.dp)
    Box (
        modifier = modifier
            .padding(8.dp)
            .clip(shape)
            .background(colorResource(R.color.light_gray))
            .padding(4.dp)
    ) {
        Row {
            leftIcon()

            Text(
                text = text,
                color = colorResource(R.color.black),
            )

            rightIcon()
        }
    }
}

@Composable
fun CreateRuleScreenBody(navController: NavController, viewModel: EditRuleViewModel, modifier: Modifier = Modifier) {
//    val ruleInfo = viewModel.getRuleInfo(id=1).collectAsState(initial=null).value
    val ruleInfo = viewModel.getRuleInfo()

    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        if (ruleInfo == null) {
            ShowLoadingText()
        } else {
            val targetTypes by ruleInfo.targetTypes.collectAsState(initial = emptyList())
            val conditionTypes by ruleInfo.conditionTypes.collectAsState(initial = emptyList())
            val actionTypes by ruleInfo.actionTypes.collectAsState(initial = emptyList())

            CreateRuleTextInput(ruleInfo)
            ShowCollapsable(targetTypes, "Targets", onClickAdd = {
                // TODO: Do something about popping up maybe or saving state or something
                navController.navigate("rules/edit/targets")
                // Consider prefetching the display names for all packages using a launched effect
            })
            ShowCollapsable(conditionTypes, "Conditions", onClickAdd = {})
            ShowCollapsable(actionTypes, "Actions", onClickAdd = {})
        }
    }
}

@Composable
fun CreateRuleScreen(navController: NavController, editRuleViewModel: EditRuleViewModel) {
    Column (
        modifier= Modifier
            .fillMaxSize()
            .background(colorResource(R.color.purple_700))
    ) {
        Utility().PercentEmpty(0.1f)
        CreateRuleScreenBody(navController, editRuleViewModel, Modifier.weight(1f))
        Utility().BottomBar("ComposeRulesActivity")
    }
}

