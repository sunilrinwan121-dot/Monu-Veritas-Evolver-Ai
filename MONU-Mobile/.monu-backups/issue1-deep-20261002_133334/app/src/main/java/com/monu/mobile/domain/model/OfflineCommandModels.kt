package com.monu.mobile.domain.model

enum class CommandSyncState {
    PENDING,
    SYNCING,
    SENT,
    ACKNOWLEDGED,
    FAILED,
    RETRY_PENDING
}

data class OfflineCommand(
    val id: String,
    val command: String,
    val createdAt: Long,
    val syncState: CommandSyncState,
    val retryCount: Int = 0,
    val lastError: String? = null
)
