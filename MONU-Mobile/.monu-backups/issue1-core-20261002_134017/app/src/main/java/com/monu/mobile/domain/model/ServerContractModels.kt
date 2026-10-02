package com.monu.mobile.domain.model

data class ServerEndpointConfig(
    val baseUrl: String = "",
    val healthPath: String = "/health",
    val capabilitiesPath: String = "/capabilities",
    val commandPath: String = "",
    val chatPath: String = "",
    val websocketUrl: String = ""
)

enum class WebSocketState {
    NOT_CONFIGURED,
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    FAILED
}

data class WebSocketStatus(
    val state: WebSocketState,
    val message: String,
    val lastEventAt: Long? = null
)
