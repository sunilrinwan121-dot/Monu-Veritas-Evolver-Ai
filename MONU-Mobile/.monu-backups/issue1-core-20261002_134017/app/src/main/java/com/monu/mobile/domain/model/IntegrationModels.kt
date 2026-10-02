package com.monu.mobile.domain.model

enum class IntegrationType {
    SERVER_API,
    LOCAL_SERVICE,
    DEVICE_SERVICE,
    EXTERNAL_SERVICE,
    UNKNOWN
}

enum class IntegrationStatus {
    UNKNOWN,
    DISCOVERED,
    CONFIGURED,
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    FAILED
}

data class IntegrationEndpoint(
    val id: String,
    val name: String,
    val type: IntegrationType,
    val status: IntegrationStatus = IntegrationStatus.UNKNOWN,
    val endpoint: String? = null
)

data class IntegrationSnapshot(
    val integrations: List<IntegrationEndpoint>,
    val generatedAt: Long = System.currentTimeMillis()
)
