package com.monu.mobile.domain.model

enum class OptimizationStatus {
    DETECTED,
    ANALYZING,
    RECOMMENDED,
    APPLIED,
    VERIFIED,
    REJECTED,
    UNKNOWN
}

enum class OptimizationConfidence {
    HIGH,
    MEDIUM,
    LOW,
    UNKNOWN
}

data class OptimizationOpportunity(
    val id: String,
    val title: String,
    val description: String,
    val source: String?,
    val status: OptimizationStatus = OptimizationStatus.UNKNOWN,
    val confidence: OptimizationConfidence = OptimizationConfidence.UNKNOWN
)

data class OptimizationRecommendation(
    val id: String,
    val opportunityId: String,
    val recommendation: String,
    val confidence: OptimizationConfidence,
    val applied: Boolean = false
)
