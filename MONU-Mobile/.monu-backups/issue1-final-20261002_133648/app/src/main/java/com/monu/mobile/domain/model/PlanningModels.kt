package com.monu.mobile.domain.model

enum class MONUPlanStatus {
    UNKNOWN,
    CREATED,
    ANALYZING,
    READY,
    EXECUTING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

enum class MONUPlanStepStatus {
    PENDING,
    READY,
    RUNNING,
    BLOCKED,
    COMPLETED,
    FAILED,
    SKIPPED,
    UNKNOWN
}

data class MONUPlanStep(
    val id: String,
    val title: String,
    val description: String,
    val status: MONUPlanStepStatus = MONUPlanStepStatus.UNKNOWN,
    val dependencies: List<String> = emptyList(),
    val estimatedPriority: Int = 0
)

data class MONUPlanRisk(
    val id: String,
    val title: String,
    val description: String,
    val severity: String = "UNKNOWN"
)

data class MONUPlan(
    val id: String,
    val goal: String,
    val status: MONUPlanStatus = MONUPlanStatus.UNKNOWN,
    val steps: List<MONUPlanStep> = emptyList(),
    val risks: List<MONUPlanRisk> = emptyList(),
    val source: String = "UNKNOWN"
)
