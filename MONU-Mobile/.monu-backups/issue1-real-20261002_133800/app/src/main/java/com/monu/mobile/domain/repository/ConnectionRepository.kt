package com.monu.mobile.domain.repository

import com.monu.mobile.data.network.MONUServerClient
import com.monu.mobile.domain.model.CapabilityStatus
import com.monu.mobile.domain.model.ConnectionStatus

class ConnectionRepository(
    private val client: MONUServerClient = MONUServerClient()
) {

    fun checkConnection(): ConnectionStatus {
        return client.checkHealth()
    }

    fun discoverCapabilities(): CapabilityStatus {
        return client.discoverCapabilities()
    }
}
