package com.monu.mobile.domain.model

enum class MONUDecisionStatus {
    UNKNOWN,
    PENDING,
    ANALYZING,
    READY,
    SELECTED,
    REJECTED,
    FAILED
}

enum class MONUDecisionFactorType {
    BENEFIT,
    RISK,
    COST,
    TIME,
    PRIORITY,
    DEPENDENCY,
    CONFIDENCE,
    CUSTOM
}

data class MONUDecisionFactor(
    val id: String,
    val type: MONUDecisionFactorType,
    val title: String,
    val value: String,
    val weight: Int = 0
)

data class MONUDecisionOption(
    val id: String,
    val title: String,
    val description: String,
    val factors: List<MONUDecisionFactor> = emptyList()
)

data class MONUDecision(
    val id: String,
    val title: String,
    val description: String,
    val status: MONUDecisionStatus = MONUDecisionStatus.UNKNOWN,
    val options: List<MONUDecisionOption> = emptyList(),
    val selectedOptionId: String? = null,
    val source: String = "UNKNOWN"
)
