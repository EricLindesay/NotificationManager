package com.example.notificationmanager

import android.app.Application
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.notificationmanager.db.entities.RuleInfo

class EditRuleViewModel(application: Application) : ViewModel() {
//    private val _selectedRule = MutableStateFlow<RuleInfo?>(null)
    var selectedRule: State<RuleInfo?>? = null

    fun setRuleState(rule: State<RuleInfo?>) {
        selectedRule = rule
    }

    fun getRuleInfo() : RuleInfo? {
        return selectedRule!!.value
    }
}