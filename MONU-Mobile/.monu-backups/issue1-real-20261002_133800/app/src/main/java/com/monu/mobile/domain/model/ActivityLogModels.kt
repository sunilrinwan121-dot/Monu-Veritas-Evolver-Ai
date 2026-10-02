package com.monu.mobile.domain.model

enum class MONUActivitySource {
    OWNER,
    MONU,
    SERVER,
    EMPLOYEE,
    SYSTEM,
    TASK,
    SECURITY,
    CONNECTION
}

enum class MONUActivitySeverity {
    INFO,
    SUCCESS,
    WARNING,
    ERROR,
    CRITICAL
}

data class MONUActivityLog(
    val id: String,
    val timestamp: Long,
    val source: MONUActivitySource,
    val severity: MONUActivitySeverity,
    val title: String,
    val description: String,
    val relatedTaskId: String? = null,
    val relatedEmployeeId: String? = null,
    val relatedProjectId: String? = null
)
