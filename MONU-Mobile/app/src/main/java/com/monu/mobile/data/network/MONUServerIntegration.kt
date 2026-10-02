package com.monu.mobile.data.network

import android.content.Context
import com.monu.mobile.domain.model.ServerEndpointConfig

class MONUServerIntegration(
    context: Context
) {
    private val client =
        MONUServerClient(
            context.applicationContext
        )

    fun configuration(): ServerEndpointConfig =
        com.monu.mobile.data.config.ServerConfigStore(
            context.applicationContext
        ).load()

    fun health(): String =
        client.checkHealth().message

    fun capabilities(): String =
        client.discoverCapabilities().rawResponse

    fun chat(
        message: String,
        conversationId: String? = null
    ): MONUServerResponse =
        client.sendChat(
            message,
            conversationId
        )

    fun command(
        command: String,
        commandId: String? = null
    ): MONUServerResponse =
        client.sendCommand(
            command,
            commandId
        )
}
