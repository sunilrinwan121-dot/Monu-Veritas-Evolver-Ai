package com.monu.mobile.domain.model

enum class MONUEmployeeStatus {
    IDLE,
    STARTING,
    WORKING,
    PROCESSING,
    WAITING,
    VERIFYING,
    COMPLETED,
    FAILED,
    OFFLINE,
    UNKNOWN
}

enum class MONUEmployeeType {
    DEVELOPER,
    RESEARCHER,
    DATA_ANALYST,
    BUSINESS,
    SUPPORT,
    MEDIA,
    SECURITY,
    SYSTEM,
    CUSTOM
}

data class MONUEmployee(
    val id: String,
    val name: String,
    val type: MONUEmployeeType,
    val status: MONUEmployeeStatus,
    val currentTask: String? = null,
    val progress: Int? = null,
    val lastActivity: String? = null,
    val error: String? = null
)
