package com.monu.mobile.domain.model

enum class MONUSessionStatus {
    ACTIVE,
    EXPIRED,
    LOGGED_OUT,
    UNKNOWN
}

data class MONUUserIdentity(
    val id: String,
    val displayName: String,
    val authenticated: Boolean,
    val source: String,
    val verified: Boolean
)

data class MONUSession(
    val id: String,
    val status: MONUSessionStatus,
    val createdAt: Long,
    val expiresAt: Long?,
    val verified: Boolean
)
