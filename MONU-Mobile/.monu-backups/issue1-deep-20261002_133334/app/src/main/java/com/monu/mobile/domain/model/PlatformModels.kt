package com.monu.mobile.domain.model

enum class PlatformComponentStatus {
    DECLARED,
    AVAILABLE,
    CONNECTED,
    ACTIVE,
    FAILED,
    UNKNOWN
}

data class PlatformComponent(
    val id: String,
    val name: String,
    val category: String,
    val status: PlatformComponentStatus = PlatformComponentStatus.UNKNOWN
)

data class PlatformCapability(
    val id: String,
    val name: String,
    val description: String,
    val components: List<String>,
    val available: Boolean = false
)

data class PlatformArchitectureSnapshot(
    val components: List<PlatformComponent>,
    val capabilities: List<PlatformCapability>
)
