package com.example.notificationmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationmanager.db.DatabaseBuilder
import com.example.notificationmanager.db.entities.Rule
import com.example.notificationmanager.db.entities.RuleInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RuleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseBuilder.getInstance(application.applicationContext)
    private val dao = db.ruleDao()

    val ruleInfos: Flow<List<RuleInfo>> = dao.getRuleInfos()

    fun setDao(ruleInfo: RuleInfo) {
        ruleInfo.dao = dao
    }

//    fun getRuleInfo(id: Long) : Flow<RuleInfo> {
//        var ruleInfo: Flow<RuleInfo>
//        if (id == 0L) {
//            ruleInfo = flowOf(RuleInfo(
//                Rule(name = "Rule ${dao.getNumRules()}")
//            ))
//        } else {
//            ruleInfo = dao.getRuleInfo(id)
//        }
//        return ruleInfo
//    }

    // Get a rule by id, if id is 0, create a new empty rule with name Rule $numRules
    fun getRuleInfo(id: Long) : Flow<RuleInfo> = flow {
        val ruleInfo: RuleInfo = withContext(Dispatchers.IO) {
            if (id == 0L) {
                val count = dao.getNumRules()
                RuleInfo(
                    Rule(name = "Rule $count")
                )
            } else {
                dao.getRuleInfo(id)
            }
        }

        setDao(ruleInfo)
        emit(ruleInfo)
    }
}

