package com.monu.mobile.domain.model

enum class MONUPermissionStatus {
    NOT_REQUESTED,
    GRANTED,
    DENIED,
    UNKNOWN
}

data class MONUPermission(
    val id: String,
    val androidPermission: String,
    val title: String,
    val description: String,
    val status: MONUPermissionStatus
)
