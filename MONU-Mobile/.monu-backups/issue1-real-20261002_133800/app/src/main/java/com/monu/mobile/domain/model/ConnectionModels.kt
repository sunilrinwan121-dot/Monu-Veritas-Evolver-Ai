package com.monu.mobile.domain.model

enum class ConnectionState {
    CONNECTED,
    DISCONNECTED,
    CHECKING,
    NOT_CONFIGURED,
    UNKNOWN
}

data class ConnectionStatus(
    val apkToServer: ConnectionState = ConnectionState.NOT_CONFIGURED,
    val serverToApk: ConnectionState = ConnectionState.UNKNOWN,
    val lastCheckedAt: Long? = null,
    val latencyMs: Long? = null,
    val message: String = "Server is not configured"
)

data class CapabilityStatus(
    val success: Boolean,
    val rawResponse: String,
    val error: String? = null
)
