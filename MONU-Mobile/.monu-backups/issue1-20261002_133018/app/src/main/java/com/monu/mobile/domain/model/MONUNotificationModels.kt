package com.monu.mobile.domain.model

enum class MONUNotificationSource {
    MOBILE,
    SERVER,
    HEARTBEAT,
    TASK,
    SYSTEM,
    SECURITY,
    APPROVAL
}

enum class MONUNotificationPriority {
    LOW,
    NORMAL,
    HIGH,
    CRITICAL
}

data class MONUNotification(
    val id: String,
    val source: MONUNotificationSource,
    val priority: MONUNotificationPriority,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val read: Boolean = false
)
