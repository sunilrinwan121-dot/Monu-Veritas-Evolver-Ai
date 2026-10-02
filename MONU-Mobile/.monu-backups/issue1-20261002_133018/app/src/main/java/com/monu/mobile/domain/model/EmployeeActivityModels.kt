package com.monu.mobile.domain.model

data class MONUEmployeeActivity(
    val id: String,
    val employeeId: String,
    val timestamp: Long,
    val title: String,
    val description: String,
    val taskId: String? = null
)
