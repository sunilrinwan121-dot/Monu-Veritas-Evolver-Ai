package com.monu.mobile.domain.model

enum class MONUContextType {
    COMMAND,
    CONVERSATION,
    PROJECT,
    TASK,
    DOCUMENT,
    SESSION,
    SYSTEM,
    UNKNOWN
}

enum class MONUContextStatus {
    AVAILABLE,
    EXPIRED,
    ARCHIVED,
    UNKNOWN
}

data class MONUContextItem(
    val id: String,
    val type: MONUContextType,
    val title: String,
    val summary: String,
    val status: MONUContextStatus = MONUContextStatus.UNKNOWN,
    val priority: Int = 0,
    val timestamp: Long? = null
)

data class MONUContextSnapshot(
    val id: String,
    val title: String,
    val items: List<MONUContextItem> = emptyList(),
    val createdAt: Long? = null
)
