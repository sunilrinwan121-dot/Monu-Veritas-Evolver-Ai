package com.monu.mobile.domain.model

enum class SyncStatus {
    IDLE,
    PENDING,
    SYNCHRONIZING,
    SYNCHRONIZED,
    CONFLICT,
    FAILED,
    UNKNOWN
}

enum class SyncDirection {
    LOCAL_TO_REMOTE,
    REMOTE_TO_LOCAL,
    BIDIRECTIONAL,
    UNKNOWN
}

data class StateSnapshot(
    val stateId: String,
    val key: String,
    val value: String,
    val version: Long,
    val updatedAt: Long = System.currentTimeMillis()
)

data class SyncRequest(
    val syncId: String,
    val direction: SyncDirection,
    val stateKeys: List<String>,
    val requestedAt: Long = System.currentTimeMillis()
)

data class SyncResult(
    val syncId: String,
    val status: SyncStatus,
    val synchronizedKeys: List<String> = emptyList(),
    val conflicts: List<String> = emptyList(),
    val message: String,
    val completedAt: Long? = null
)

data class SyncReport(
    val results: List<SyncResult>,
    val total: Int,
    val synchronized: Int,
    val conflicts: Int,
    val failed: Int,
    val unknown: Int
)
