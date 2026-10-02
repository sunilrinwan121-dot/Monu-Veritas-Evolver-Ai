package com.monu.mobile.domain.model

enum class ServiceType {
    COMMAND,
    STORAGE,
    NETWORK,
    NOTIFICATION,
    WORKFLOW,
    BACKGROUND,
    UNKNOWN
}

enum class ServiceStatus {
    UNKNOWN,
    REGISTERED,
    STARTING,
    RUNNING,
    STOPPED,
    FAILED
}

data class ServiceDescriptor(
    val id: String,
    val name: String,
    val type: ServiceType,
    val status: ServiceStatus = ServiceStatus.UNKNOWN
)

data class ServiceCoordinationSnapshot(
    val services: List<ServiceDescriptor>,
    val generatedAt: Long = System.currentTimeMillis()
)
