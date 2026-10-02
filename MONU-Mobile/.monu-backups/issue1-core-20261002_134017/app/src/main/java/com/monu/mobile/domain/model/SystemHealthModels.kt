package com.monu.mobile.domain.model

enum class MONUHealthStatus {
    HEALTHY,
    WARNING,
    CRITICAL,
    UNKNOWN
}

data class MONUHealthMetric(
    val id: String,
    val title: String,
    val description: String,
    val status: MONUHealthStatus,
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class MONUSystemHealthReport(
    val generatedAt: Long,
    val overallStatus: MONUHealthStatus,
    val metrics: List<MONUHealthMetric>
)
