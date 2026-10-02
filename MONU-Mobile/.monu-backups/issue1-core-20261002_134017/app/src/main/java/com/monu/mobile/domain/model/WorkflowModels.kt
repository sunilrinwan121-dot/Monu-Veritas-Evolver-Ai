package com.monu.mobile.domain.model

enum class MONUWorkflowStatus {
    DRAFT,
    ENABLED,
    DISABLED,
    RUNNING,
    COMPLETED,
    FAILED,
    UNKNOWN
}

enum class MONUWorkflowTriggerType {
    MANUAL,
    COMMAND,
    SCHEDULE,
    EVENT,
    SERVER,
    CONDITION
}

data class MONUWorkflowStep(
    val id: String,
    val title: String,
    val action: String,
    val order: Int,
    val enabled: Boolean = true
)

data class MONUWorkflow(
    val id: String,
    val name: String,
    val description: String,
    val status: MONUWorkflowStatus = MONUWorkflowStatus.DRAFT,
    val trigger: MONUWorkflowTriggerType = MONUWorkflowTriggerType.MANUAL,
    val steps: List<MONUWorkflowStep> = emptyList(),
    val lastRunTimestamp: Long? = null
)

data class MONUWorkflowRun(
    val id: String,
    val workflowId: String,
    val status: MONUWorkflowStatus,
    val startedAt: Long? = null,
    val completedAt: Long? = null,
    val message: String? = null
)
