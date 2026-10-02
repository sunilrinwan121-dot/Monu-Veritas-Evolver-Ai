package com.monu.mobile.domain.model

enum class IntelligenceStatus {
    IDLE,
    COLLECTING,
    ANALYZING,
    READY,
    INCOMPLETE,
    UNKNOWN
}

enum class IntelligenceConfidence {
    VERIFIED,
    HIGH,
    MEDIUM,
    LOW,
    UNKNOWN
}

data class IntelligenceSignal(
    val id: String,
    val source: String,
    val type: String,
    val value: String?,
    val timestamp: Long?,
    val confidence: IntelligenceConfidence = IntelligenceConfidence.UNKNOWN
)

data class IntelligenceInsight(
    val id: String,
    val title: String,
    val summary: String,
    val sources: List<String>,
    val confidence: IntelligenceConfidence,
    val status: IntelligenceStatus = IntelligenceStatus.UNKNOWN
)

data class IntelligenceSnapshot(
    val id: String,
    val signals: List<IntelligenceSignal>,
    val insights: List<IntelligenceInsight>,
    val status: IntelligenceStatus
)
