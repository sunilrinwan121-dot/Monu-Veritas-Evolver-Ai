package com.monu.mobile.feature.platform

import com.monu.mobile.domain.model.PlatformArchitectureSnapshot
import com.monu.mobile.domain.model.PlatformCapability
import com.monu.mobile.domain.model.PlatformComponent
import com.monu.mobile.domain.model.PlatformComponentStatus

class MONUPlatformCoordinator {

    fun components(): List<PlatformComponent> {
        return listOf(
            PlatformComponent(
                id = "android-runtime",
                name = "Android Runtime",
                category = "platform",
                status = PlatformComponentStatus.AVAILABLE
            ),
            PlatformComponent(
                id = "local-memory",
                name = "Local Memory",
                category = "storage",
                status = PlatformComponentStatus.AVAILABLE
            ),
            PlatformComponent(
                id = "conversation-store",
                name = "Conversation Store",
                category = "storage",
                status = PlatformComponentStatus.AVAILABLE
            ),
            PlatformComponent(
                id = "network-monitor",
                name = "Network Monitor",
                category = "connectivity",
                status = PlatformComponentStatus.AVAILABLE
            ),
            PlatformComponent(
                id = "gemini-provider",
                name = "Gemini Provider",
                category = "intelligence",
                status = PlatformComponentStatus.DECLARED
            ),
            PlatformComponent(
                id = "websocket-provider",
                name = "WebSocket Provider",
                category = "realtime",
                status = PlatformComponentStatus.DECLARED
            )
        )
    }

    fun capabilities(): List<PlatformCapability> {
        return listOf(
            PlatformCapability(
                id = "local-runtime",
                name = "Local Runtime",
                description = "Provides verified Android runtime availability.",
                components = listOf("android-runtime"),
                available = true
            ),
            PlatformCapability(
                id = "local-memory",
                name = "Local Memory",
                description = "Provides local application memory services.",
                components = listOf("local-memory"),
                available = true
            ),
            PlatformCapability(
                id = "conversation-storage",
                name = "Conversation Storage",
                description = "Provides local conversation persistence.",
                components = listOf("conversation-store"),
                available = true
            ),
            PlatformCapability(
                id = "network-awareness",
                name = "Network Awareness",
                description = "Provides application network state awareness.",
                components = listOf("network-monitor"),
                available = true
            ),
            PlatformCapability(
                id = "gemini-intelligence",
                name = "Gemini Intelligence",
                description = "Requires a configured and reachable Gemini provider.",
                components = listOf("gemini-provider"),
                available = false
            ),
            PlatformCapability(
                id = "realtime-events",
                name = "Realtime Events",
                description = "Requires a configured and connected WebSocket provider.",
                components = listOf("websocket-provider"),
                available = false
            )
        )
    }

    fun architectureSnapshot(): PlatformArchitectureSnapshot {
        return PlatformArchitectureSnapshot(
            components = components(),
            capabilities = capabilities()
        )
    }
}
