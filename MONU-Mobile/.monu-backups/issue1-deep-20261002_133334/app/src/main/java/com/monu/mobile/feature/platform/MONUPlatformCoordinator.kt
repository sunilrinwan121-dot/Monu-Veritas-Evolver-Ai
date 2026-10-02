package com.monu.mobile.feature.platform

import com.monu.mobile.domain.model.PlatformArchitectureSnapshot
import com.monu.mobile.domain.model.PlatformCapability
import com.monu.mobile.domain.model.PlatformComponent

class MONUPlatformCoordinator {

    fun components(): List<PlatformComponent> {
        return emptyList()
    }

    fun capabilities(): List<PlatformCapability> {
        return emptyList()
    }

    fun architectureSnapshot(): PlatformArchitectureSnapshot {
        return PlatformArchitectureSnapshot(
            components = components(),
            capabilities = capabilities()
        )
    }
}
