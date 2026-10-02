package com.monu.mobile.domain.model

enum class MONUCapabilityStatus {
    AVAILABLE,
    UNAVAILABLE,
    REQUIRES_PERMISSION,
    REQUIRES_USER_ACTION,
    UNKNOWN
}

data class MONUDeviceCapability(
    val id: String,
    val title: String,
    val description: String,
    val status: MONUCapabilityStatus,
    val verified: Boolean
)
