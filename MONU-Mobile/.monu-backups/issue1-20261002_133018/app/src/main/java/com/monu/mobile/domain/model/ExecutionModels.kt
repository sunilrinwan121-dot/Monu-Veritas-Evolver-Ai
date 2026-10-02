package com.monu.mobile.domain.model

enum class ExecutionStatus {
    CREATED,
    QUEUED,
    READY,
    RUNNING,
    WAITING,
    SUCCEEDED,
    FAILED,
    CANCELLED,
    UNKNOWN
}

enum class ExecutionType {
    COMMAND,
    WORKFLOW,
    PLAN,
    RULE_ACTION,
    TASK,
    SYSTEM_ACTION
}

data class ExecutionRequest(
    val executionId: String,
    val type: ExecutionType,
    val title: String,
    val payload: String? = null,
    val requestedAt: Long = System.currentTimeMillis()
)

data class ExecutionStep(
    val stepId: String,
    val title: String,
    val status: ExecutionStatus = ExecutionStatus.CREATED,
    val startedAt: Long? = null,
    val completedAt: Long? = null,
    val message: String? = null
)

data class ExecutionRecord(
    val executionId: String,
    val type: ExecutionType,
    val title: String,
    val status: ExecutionStatus,
    val steps: List<ExecutionStep> = emptyList(),
    val startedAt: Long? = null,
    val completedAt: Long? = null,
    val error: String? = null
)

data class ExecutionReport(
    val executions: List<ExecutionRecord>,
    val total: Int,
    val running: Int,
    val succeeded: Int,
    val failed: Int,
    val unknown: Int
)
