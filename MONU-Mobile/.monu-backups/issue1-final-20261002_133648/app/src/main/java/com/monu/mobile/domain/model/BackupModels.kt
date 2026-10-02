package com.monu.mobile.domain.model

enum class MONUBackupStatus {
    NEVER_CREATED,
    PREPARING,
    RUNNING,
    COMPLETED,
    FAILED,
    UNKNOWN
}

enum class MONUBackupScope {
    SETTINGS,
    OFFLINE_COMMANDS,
    LOCAL_DATABASE,
    PROJECT_METADATA
}

data class MONUBackupInfo(
    val id: String,
    val createdAt: Long?,
    val status: MONUBackupStatus,
    val scopes: List<MONUBackupScope>,
    val locationDescription: String
)

enum class MONURestoreStatus {
    NOT_STARTED,
    PREPARING,
    VERIFYING,
    RESTORING,
    COMPLETED,
    FAILED
}
