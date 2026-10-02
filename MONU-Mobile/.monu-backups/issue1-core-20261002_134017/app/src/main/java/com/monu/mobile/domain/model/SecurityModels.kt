package com.monu.mobile.domain.model

enum class MONUSecurityStatus {
    SECURE,
    WARNING,
    RISK,
    CRITICAL,
    UNKNOWN
}

enum class MONUSecurityCategory {
    APPLICATION,
    PERMISSIONS,
    NETWORK,
    SERVER,
    STORAGE,
    AUTHENTICATION,
    DEVICE,
    SESSION
}

data class MONUSecurityFinding(
    val id: String,
    val category: MONUSecurityCategory,
    val status: MONUSecurityStatus,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val verified: Boolean = false
)

data class MONUSecurityReport(
    val findings: List<MONUSecurityFinding>,
    val generatedAt: Long = System.currentTimeMillis()
)
