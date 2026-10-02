package com.monu.mobile.domain.model

data class RealtimeEvent(
    val type: String,
    val payload: String,
    val receivedAt: Long =
        System.currentTimeMillis()
)
