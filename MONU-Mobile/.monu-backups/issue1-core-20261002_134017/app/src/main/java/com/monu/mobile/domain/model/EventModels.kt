package com.monu.mobile.domain.model

enum class MONUEventType {
    COMMAND,
    WORKFLOW,
    EXECUTION,
    VERIFICATION,
    SECURITY,
    SYSTEM,
    USER,
    NETWORK,
    UNKNOWN
}

enum class MONUEventStatus {
    RECEIVED,
    PROCESSING,
    PROCESSED,
    FAILED,
    IGNORED,
    UNKNOWN
}

data class MONUEvent(
    val eventId: String,
    val type: MONUEventType,
    val title: String,
    val payload: String? = null,
    val source: String = "LOCAL",
    val timestamp: Long = System.currentTimeMillis(),
    val status: MONUEventStatus = MONUEventStatus.RECEIVED
)

data class EventInsight(
    val eventId: String,
    val category: String,
    val summary: String,
    val confidence: Int = 0
)

data class EventReport(
    val events: List<MONUEvent>,
    val total: Int,
    val received: Int,
    val processed: Int,
    val failed: Int,
    val unknown: Int
)
