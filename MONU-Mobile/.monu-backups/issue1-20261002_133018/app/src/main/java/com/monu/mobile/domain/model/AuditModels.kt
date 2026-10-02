package com.monu.mobile.domain.model

enum class MONUAuditActor {
    OWNER,
    MONU,
    SERVER,
    EMPLOYEE,
    SYSTEM,
    UNKNOWN
}

enum class MONUAuditAction {
    COMMAND_RECEIVED,
    COMMAND_EXECUTED,
    TASK_CREATED,
    TASK_COMPLETED,
    TASK_FAILED,
    LOGIN,
    LOGOUT,
    CONFIGURATION_CHANGED,
    PERMISSION_CHANGED,
    SECURITY_EVENT,
    FILE_TRANSFER,
    BACKUP,
    RESTORE,
    UNKNOWN
}

enum class MONUAuditResult {
    SUCCESS,
    FAILURE,
    PENDING,
    UNKNOWN
}

data class MONUAuditEntry(
    val id: String,
    val timestamp: Long,
    val actor: MONUAuditActor,
    val action: MONUAuditAction,
    val result: MONUAuditResult,
    val title: String,
    val description: String,
    val metadata: Map<String, String> = emptyMap()
)
