package com.monu.mobile.domain.model

enum class MONURuleStatus {
    DRAFT,
    ENABLED,
    DISABLED,
    TRIGGERED,
    FAILED,
    UNKNOWN
}

enum class MONURuleConditionType {
    COMMAND_MATCH,
    EVENT_MATCH,
    STATUS_CHANGE,
    TIME,
    CONNECTION,
    CUSTOM
}

enum class MONURuleActionType {
    CREATE_TASK,
    START_WORKFLOW,
    SEND_NOTIFICATION,
    ASSIGN_EMPLOYEE,
    RECORD_ACTIVITY,
    CUSTOM
}

data class MONURuleCondition(
    val type: MONURuleConditionType,
    val expression: String
)

data class MONURuleAction(
    val type: MONURuleActionType,
    val payload: String
)

data class MONURule(
    val id: String,
    val name: String,
    val description: String,
    val status: MONURuleStatus = MONURuleStatus.DRAFT,
    val conditions: List<MONURuleCondition> = emptyList(),
    val actions: List<MONURuleAction> = emptyList(),
    val lastTriggeredTimestamp: Long? = null
)
