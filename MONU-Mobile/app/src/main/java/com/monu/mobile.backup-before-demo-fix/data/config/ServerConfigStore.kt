package com.monu.mobile.data.config

import android.content.Context
import com.monu.mobile.domain.model.ServerEndpointConfig

class ServerConfigStore(
    context: Context
) {

    private val preferences =
        context.getSharedPreferences(
            "monu_server_config",
            Context.MODE_PRIVATE
        )

    fun save(
        config: ServerEndpointConfig
    ) {
        preferences.edit()
            .putString("baseUrl", config.baseUrl)
            .putString("healthPath", config.healthPath)
            .putString("capabilitiesPath", config.capabilitiesPath)
            .putString("commandPath", config.commandPath)
            .putString("chatPath", config.chatPath)
            .putString("websocketUrl", config.websocketUrl)
            .apply()
    }

    fun load(): ServerEndpointConfig {
        return ServerEndpointConfig(
            baseUrl =
                preferences.getString(
                    "baseUrl",
                    ""
                ) ?: "",

            healthPath =
                preferences.getString(
                    "healthPath",
                    "/health"
                ) ?: "/health",

            capabilitiesPath =
                preferences.getString(
                    "capabilitiesPath",
                    "/capabilities"
                ) ?: "/capabilities",

            commandPath =
                preferences.getString(
                    "commandPath",
                    ""
                ) ?: "",

            chatPath =
                preferences.getString(
                    "chatPath",
                    ""
                ) ?: "",

            websocketUrl =
                preferences.getString(
                    "websocketUrl",
                    ""
                ) ?: ""
        )
    }
}
