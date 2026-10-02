package com.monu.mobile.domain.model

enum class MONUDiagnosticStatus {
    PASS,
    WARNING,
    FAIL,
    UNKNOWN
}

enum class MONUDiagnosticCategory {
    APPLICATION,
    STORAGE,
    NETWORK,
    PERMISSIONS,
    DATABASE,
    SERVER,
    SECURITY
}

data class MONUDiagnosticResult(
    val id: String,
    val category: MONUDiagnosticCategory,
    val title: String,
    val description: String,
    val status: MONUDiagnosticStatus,
    val timestamp: Long = System.currentTimeMillis()
)

data class MONUDiagnosticReport(
    val startedAt: Long,
    val completedAt: Long,
    val results: List<MONUDiagnosticResult>
)
