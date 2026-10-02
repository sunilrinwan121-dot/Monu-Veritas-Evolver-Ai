package com.monu.mobile.domain.model

enum class RecoveryStatus {
    IDLE,
    FAILURE_DETECTED,
    CHECKPOINT_AVAILABLE,
    RECOVERY_PLANNED,
    RECOVERING,
    RECOVERED,
    FAILED,
    UNKNOWN
}

data class RecoveryCheckpoint(
    val id: String,
    val source: String,
    val createdAt: Long,
    val verified: Boolean = false
)

data class RecoveryPlan(
    val id: String,
    val target: String,
    val steps: List<String>,
    val status: RecoveryStatus = RecoveryStatus.UNKNOWN
)

data class RecoveryResult(
    val planId: String,
    val status: RecoveryStatus,
    val evidence: String? = null
)
