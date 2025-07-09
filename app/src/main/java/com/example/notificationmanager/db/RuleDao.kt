package com.example.notificationmanager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.notificationmanager.db.entities.Rule
import com.example.notificationmanager.db.entities.action.RuleAction
import com.example.notificationmanager.db.entities.action.RuleActionType
import com.example.notificationmanager.db.entities.action.RuleActionTypeEnum
import com.example.notificationmanager.db.entities.condition.RuleConditionType
import com.example.notificationmanager.db.entities.RuleInfo
import com.example.notificationmanager.db.entities.target.RuleTargetType
import com.example.notificationmanager.db.entities.RuleWithAction
import com.example.notificationmanager.db.entities.action.RuleTargetAttributeLink
import com.example.notificationmanager.db.entities.target.RuleTarget
import com.example.notificationmanager.db.entities.target.RuleTargetAttribute
import com.example.notificationmanager.db.entities.target.RuleTargetTypeEnum
import kotlinx.coroutines.flow.Flow


@Dao
interface RuleDao {
    @Transaction
    @Query("SELECT * FROM Rule WHERE id = :ruleId")
    suspend fun getRuleWithActionTypes(ruleId: Int): RuleWithAction

    @Query("SELECT * FROM Rule")
    fun getRules(): Flow<List<Rule>>

    @Query("SELECT * FROM Rule WHERE id = :id")
    fun getRuleInfo(id: Long): RuleInfo

    @Query("SELECT COUNT(*) FROM Rule")
    suspend fun getNumRules(): Long

    @Query("SELECT * FROM Rule")
    fun getRuleInfos(): Flow<List<RuleInfo>>

    // Get the action types depending on the rule id
    @Query("""
        SELECT * 
        FROM RuleActionType 
        JOIN RuleAction ON RuleAction.actionTypeId == RuleActionType.id
        WHERE RuleAction.ruleId == :ruleId
        ORDER BY RuleActionType.id
    """)
    fun getActionTypes(ruleId: Long): Flow<List<RuleActionType>>

    @Query("""
        SELECT * 
        FROM RuleConditionType 
        JOIN RuleCondition ON RuleCondition.conditionTypeId == RuleConditionType.id
        WHERE RuleCondition.ruleId == :ruleId
        ORDER BY RuleConditionType.id
    """)
    fun getConditionTypes(ruleId: Long): Flow<List<RuleConditionType>>

    @Query("""
        SELECT * 
        FROM RuleTargetType 
        JOIN RuleTarget ON RuleTarget.targetTypeId == RuleTargetType.id
        WHERE RuleTarget.ruleId == :ruleId
        ORDER BY RuleTargetType.id
    """)
    fun getTargetTypesFlow(ruleId: Long): Flow<List<RuleTargetType>>

    @Query("""
        SELECT * 
        FROM RuleTargetType 
        JOIN RuleTarget ON RuleTarget.targetTypeId == RuleTargetType.id
        WHERE RuleTarget.ruleId == :ruleId
        ORDER BY RuleTargetType.id
    """)
    fun getTargetTypes(ruleId: Long): List<RuleTargetType>

    @Query("""
        SELECT * 
        FROM RuleTargetType 
        JOIN RuleTarget ON RuleTarget.targetTypeId == RuleTargetType.id
        WHERE RuleTarget.ruleId == :ruleId AND RuleTargetType.name == :name
        ORDER BY RuleTargetType.id
    """)
    fun getTargetTypesWithName(ruleId: Long, name: String): List<RuleTargetType>

    @Query("""
        SELECT *
        FROM RuleTargetAttribute
        JOIN RuleTargetAttributeLink ON RuleTargetAttributeLink.targetAttributeId == RuleTargetAttribute.id
        WHERE RuleTargetAttributeLink.ruleId == :ruleId
        ORDER BY RuleTargetAttribute.id
    """)
    fun getTargetAttributes(ruleId: Long): List<RuleTargetAttribute>

    @Query("""
        SELECT *
        FROM RuleTargetAttribute
        JOIN RuleTargetAttributeLink ON RuleTargetAttributeLink.targetAttributeId == RuleTargetAttribute.id
        WHERE RuleTargetAttributeLink.ruleId == :ruleId AND RuleTargetAttribute.name == :type
        ORDER BY RuleTargetAttribute.id
    """)
    fun getTargetAttributesWithType(ruleId: Long, type: String): List<RuleTargetAttribute>

    @Query("""
        SELECT (COUNT(*) > 0)
        FROM RuleTargetType
        JOIN RuleTarget ON RuleTarget.targetTypeId == RuleTargetType.id
        WHERE RuleTargetType.name == "ALL" AND RuleTarget.ruleId == :ruleId
    """)
    suspend fun hasTargetAll(ruleId: Long): Boolean

    @Insert
    suspend fun insertRule(rule: Rule): Long

    @Insert
    suspend fun insertRuleActionTypes(ruleActionTypes: List<RuleActionType>): List<Long>

    @Insert
    suspend fun insertRuleAction(ruleAction: RuleAction): Long

    @Insert
    suspend fun insertRuleTargetTypes(ruleTargetTypes: List<RuleTargetType>): List<Long>

    @Insert
    suspend fun insertRuleTargetAttributes(ruleTargetAttributes: List<RuleTargetAttribute>): List<Long>

    @Insert
    suspend fun insertRuleTarget(ruleTargets: List<RuleTarget>): List<Long>

    @Insert
    suspend fun insertRuleTargetAttributeLink(ruleTargetAttributeLinks: List<RuleTargetAttributeLink>)

    suspend fun insertRuleWithAction(ruleWithAction: RuleWithAction) {
        val ruleId = insertRule(ruleWithAction.rule)

        val actionIds = insertRuleActionTypes(ruleWithAction.actionTypes)

        for (actionId in actionIds) {
            insertRuleAction(RuleAction(ruleId, actionId))
        }
    }

    suspend fun insertDefaultTest() {
        val rule = Rule(name="New Rule")
        val action1 = RuleActionType(name= RuleActionTypeEnum.ALLOW)
        val action2 = RuleActionType(name= RuleActionTypeEnum.SILENCE)
        val action3 = RuleActionType(name= RuleActionTypeEnum.BLOCK)
        val ruleWithAction = RuleWithAction(rule, listOf(action1, action2, action3))

        insertRuleWithAction(ruleWithAction)
    }

    suspend fun insertDummyRuleWithTargets() {
        val rule = Rule(name="Target Rule")
        val target1 = RuleTargetType(name=RuleTargetTypeEnum.PACKAGE)
        val attribute1 = RuleTargetAttribute(name="package_name", value="com.example.notificationmanager")
        val attribute2 = RuleTargetAttribute(name="package_name", value="com.whatsapp")
        val target2 = RuleTargetType(name=RuleTargetTypeEnum.ALL)

        // Add rule
        val ruleId = insertRule(rule)

        // Add targets
        val targetIds: List<Long> = insertRuleTargetTypes(listOf(target1, target2));
        val ruleTargets: ArrayList<RuleTarget> = ArrayList()
        for (targetId in targetIds) {
            ruleTargets.add(RuleTarget(ruleId, targetId))
        }
        insertRuleTarget(ruleTargets)

        // Add attributes
        val attributeIds: List<Long> = insertRuleTargetAttributes(listOf(attribute1, attribute2));
        val ruleTargetAttributes: ArrayList<RuleTargetAttributeLink> = ArrayList()
        for (attributeId in attributeIds) {
            ruleTargetAttributes.add(RuleTargetAttributeLink(ruleId, attributeId))
        }
        insertRuleTargetAttributeLink(ruleTargetAttributes)

    }

    suspend fun insertAdCapTarget() {
        var ids = insertRuleTargetAttributes(listOf(RuleTargetAttribute(name="package_name", value="com.kongregate.mobile.adventurecapitalist.google")))
        var id = ids.get(0)

        insertRuleTargetAttributeLink(listOf(RuleTargetAttributeLink(2, id)))
    }
}