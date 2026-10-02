package com.monu.mobile.domain.model

enum class MONUTaskStatus {
    QUEUED,
    STARTING,
    RUNNING,
    PROCESSING,
    VERIFYING,
    COMPLETED,
    FAILED,
    DIAGNOSING,
    RECOVERING,
    RETRYING,
    CANCELLED
}

enum class MONUTaskPriority {
    LOW,
    NORMAL,
    HIGH,
    CRITICAL
}

data class MONUTask(
    val id: String,
    val title: String,
    val description: String = "",
    val status: MONUTaskStatus = MONUTaskStatus.QUEUED,
    val priority: MONUTaskPriority = MONUTaskPriority.NORMAL,
    val progress: Int = 0,
    val currentStage: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val source: String = "LOCAL"
)
