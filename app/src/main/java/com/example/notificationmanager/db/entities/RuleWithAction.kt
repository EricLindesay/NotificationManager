package com.example.notificationmanager.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.notificationmanager.db.entities.action.RuleAction
import com.example.notificationmanager.db.entities.action.RuleActionType


data class RuleWithAction(
    @Embedded val rule: Rule,

    // Implement a class which has lazy loading,
    // if gets the rule and the action types and stuff
    // but only the action types if you actually want it
    // it loads all action types in at once though
    // also for the other things
    // so if you only look at the types and not attributes, you're fine
    // but then theres not really a point since you'll nearly always want them all
    // so idk   

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RuleAction::class,
            parentColumn = "ruleId",
            entityColumn = "actionTypeId"
        )
    )
    val actionTypes: List<RuleActionType>
)
