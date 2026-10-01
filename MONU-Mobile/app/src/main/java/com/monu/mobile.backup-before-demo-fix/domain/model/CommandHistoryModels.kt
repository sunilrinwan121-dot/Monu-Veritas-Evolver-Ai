package com.monu.mobile.domain.model

enum class MONUCommandStatus {
    RECEIVED,
    PROCESSING,
    COMPLETED,
    FAILED,
    UNKNOWN
}

data class MONUCommandHistoryEntry(
    val id: String,
    val command: String,
    val timestamp: Long,
    val status: MONUCommandStatus,
    val resultSummary: String? = null,
    val source: String = "OWNER",
    val verified: Boolean = false
)

data class MONUCommandPattern(
    val pattern: String,
    val count: Int,
    val description: String
)
