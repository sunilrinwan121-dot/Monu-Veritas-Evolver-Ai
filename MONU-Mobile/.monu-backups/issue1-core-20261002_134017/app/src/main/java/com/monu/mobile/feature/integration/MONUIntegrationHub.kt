package com.monu.mobile.feature.integration

import com.monu.mobile.domain.model.IntegrationEndpoint
import com.monu.mobile.domain.model.IntegrationSnapshot

class MONUIntegrationHub {

    fun snapshot(
        integrations: List<IntegrationEndpoint> = emptyList()
    ): IntegrationSnapshot {
        return IntegrationSnapshot(
            integrations = integrations
        )
    }

    fun knownIntegrations(
        integrations: List<IntegrationEndpoint>
    ): List<IntegrationEndpoint> {
        return integrations
    }
}
