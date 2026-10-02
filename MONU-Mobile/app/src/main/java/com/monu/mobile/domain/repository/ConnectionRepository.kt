package com.monu.mobile.domain.repository

import android.content.Context
import com.monu.mobile.data.network.MONUServerClient
import com.monu.mobile.domain.model.CapabilityStatus
import com.monu.mobile.domain.model.ConnectionStatus

class ConnectionRepository(
    context: Context
) {
    private val client =
        MONUServerClient(
            context.applicationContext
        )

    fun checkConnection(): ConnectionStatus =
        client.checkHealth()

    fun discoverCapabilities(): CapabilityStatus =
        client.discoverCapabilities()

    fun sendChat(
        message: String,
        conversationId: String? = null
    ) =
        client.sendChat(
            message,
            conversationId
        )

    fun sendCommand(
        command: String,
        commandId: String? = null
    ) =
        client.sendCommand(
            command,
            commandId
        )
}
