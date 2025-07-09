package com.example.notificationmanager.db.entities

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.room.Embedded
import androidx.room.Ignore
import com.example.notificationmanager.R
import com.example.notificationmanager.db.RuleDao
import com.example.notificationmanager.db.entities.action.RuleActionType
import com.example.notificationmanager.db.entities.condition.RuleConditionType
import com.example.notificationmanager.db.entities.target.RuleTargetType
import com.example.notificationmanager.db.entities.target.RuleTargetTypeEnum
import com.example.notificationmanager.db.entities.target.RuleTargetTypeEnumTypeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class RuleInfo(
    @Embedded
    var rule: Rule
){
    @Ignore
    var dao: RuleDao? = null

    @Ignore
    private var _actionTypes: MutableStateFlow<List<RuleActionType>>? = null
    @Ignore
    private var _conditionTypes: Flow<List<RuleConditionType>>? = null
    @Ignore
    private var _targetTypes: MutableStateFlow<List<RuleTargetType>>? = null

    // Consider this being a function where you pass the dao
    val actionTypes: MutableStateFlow<List<RuleActionType>>
        get() {
            if (_actionTypes == null) {
                _actionTypes = MutableStateFlow(emptyList());
                // Access the dao
                if (dao == null) {
                    throw IllegalStateException("Cannot access rule's action types without setting " +
                            "the dao")
                }
//                _actionTypes = dao!!.getActionTypes(rule.id).collect() {actionTypes}
                CoroutineScope(Dispatchers.IO).launch {
                    dao!!.getActionTypes(rule.id).collect { _actionTypes!!.value = it }
                }
            }
            return _actionTypes!!
        }

    // Consider this being a function where you pass the dao
    val conditionTypes: Flow<List<RuleConditionType>>
        get() {
            if (_conditionTypes == null) {
                // Access the dao
                if (dao == null) {
                    throw IllegalStateException("Cannot access rule's condition types without setting " +
                            "the dao")
                }
                _conditionTypes = dao!!.getConditionTypes(rule.id)
            }
            return _conditionTypes!!
        }

    // Consider this being a function where you pass the dao
    val targetTypes: MutableStateFlow<List<RuleTargetType>>
        get() {
            if (_targetTypes == null) {
                _targetTypes = MutableStateFlow(emptyList())
                // Access the dao
                if (dao == null) {
                    throw IllegalStateException("Cannot access rule's target types without setting " +
                            "the dao")
                }
                CoroutineScope(Dispatchers.IO).launch {
                    dao!!.getTargetTypesFlow(rule.id).collect { _targetTypes!!.value = it }
                }
            }
            return _targetTypes!!
        }

    @Ignore
    var ruleTargetInfos: MutableStateFlow<List<RuleTargetInfo>>? = null

    fun hasTargetAll() : MutableStateFlow<Boolean> {
        var ans: MutableStateFlow<Boolean> = MutableStateFlow(false)

        CoroutineScope(Dispatchers.IO).launch {
            ans.value = dao!!.hasTargetAll(rule.id)
        }

        return ans
    }

    fun getRuleTargetInfoPackages(context: Context): MutableStateFlow<List<RuleTargetInfo>> {
        if (ruleTargetInfos != null) {
            return ruleTargetInfos!!
        }
        ruleTargetInfos = MutableStateFlow(emptyList())

        CoroutineScope(Dispatchers.IO).launch {
//            val targetTypes = dao!!.getTargetTypesWithName(rule.id, RuleTargetTypeEnumTypeConverter().fromType(RuleTargetTypeEnum.ALL))
            val targetAttributes = dao!!.getTargetAttributesWithType(rule.id, type="package_name")
            // For this one, ordering doesn't actually matter

//            for (targetType in targetTypes) {
//                ruleTargetInfos!!.value += RuleTargetInfo(
//                    context,
//                    packageName = "All Apps",
//                    display = "All Apps",
//                    drawable = ContextCompat.getDrawable(context, R.drawable.all_inclusive_icon),
//                    selected = true,
//                )
//            }

            for (attribute in targetAttributes) {
                if (attribute.name == "package_name") {
                    ruleTargetInfos!!.value += RuleTargetInfo(
                        context,
                        packageName = attribute.value,
                        selected = true,
                    )
                } else {
                    Log.d("RuleInfo", "Categories not supported yet")
                }
            }
//                dao!!.getTargetTypesFlow(rule.id).collect { _targetTypes!!.value = it }
        }
        // Do this in coroutine scope
        // Get the rule target types and attributes
        // Generate targetInfos from it
        return ruleTargetInfos!!
    }

    fun addAction(actionType: RuleActionType/*, actionAttributes: List<RuleActionAttribute>*/) {
        actionTypes  // get this to set the _actionTypes

        _actionTypes!!.value = _actionTypes!!.value + actionType;
    }

    fun addTarget(targetType: RuleTargetType/*, actionAttributes: List<RuleActionAttribute>*/) {
        targetTypes  // get this to set the _targetTypes

        _targetTypes!!.value = _targetTypes!!.value + targetType;
    }
}