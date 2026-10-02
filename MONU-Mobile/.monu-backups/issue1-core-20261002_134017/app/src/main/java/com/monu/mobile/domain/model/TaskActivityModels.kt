package com.monu.mobile.domain.model

data class TaskActivity(
    val id: String,
    val taskId: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
