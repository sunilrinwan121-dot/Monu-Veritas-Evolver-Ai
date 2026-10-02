package com.monu.mobile.domain.model

enum class MONUProjectStatus {
    PLANNING,
    ACTIVE,
    PAUSED,
    BLOCKED,
    COMPLETED,
    ARCHIVED
}

data class MONUProject(
    val id: String,
    val name: String,
    val description: String = "",
    val status: MONUProjectStatus = MONUProjectStatus.PLANNING,
    val progress: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class MONUProjectSummary(
    val projectId: String,
    val goals: Int = 0,
    val tasks: Int = 0,
    val files: Int = 0,
    val employees: Int = 0,
    val reports: Int = 0
)
