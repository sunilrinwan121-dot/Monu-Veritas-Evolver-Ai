package com.monu.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_commands")
data class OfflineCommandEntity(
    @PrimaryKey
    val id: String,

    val command: String,

    val createdAt: Long,

    val syncState: String,

    val retryCount: Int = 0,

    val lastError: String? = null
)
