
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
    PROJECT_METADATA,
    WORKFLOWS
}

data class MONUBackupInfo(
    val id: String,
    val createdAt: Long?,
    val status: MONUBackupStatus,
    val scopes: List<MONUBackupScope>,
    val locationDescription: String,
    val filePath: String? = null,
    val checksum: String? = null,
    val sizeBytes: Long = 0L
)

enum class MONURestoreStatus {
    NOT_STARTED,
    PREPARING,
    VERIFYING,
    RESTORING,
    COMPLETED,
    FAILED
}
