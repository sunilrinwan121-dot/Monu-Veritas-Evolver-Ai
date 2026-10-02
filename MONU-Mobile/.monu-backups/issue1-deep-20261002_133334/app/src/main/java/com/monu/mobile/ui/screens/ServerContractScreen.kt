package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.data.config.ServerConfigStore
import com.monu.mobile.domain.model.ServerEndpointConfig

@Composable
fun ServerContractScreen() {

    val context =
        LocalContext.current

    val store =
        remember {
            ServerConfigStore(context)
        }

    var config by remember {
        mutableStateOf(
            store.load()
        )
    }

    var message by remember {
        mutableStateOf(
            "Load your REAL MONU Server contract here."
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        Text(
            "SERVER API CONTRACT",
            style =
                MaterialTheme.typography
                    .headlineSmall
        )

        Text(
            "These values must match the real MONU Server. Nothing is simulated."
        )

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = config.baseUrl,
            onValueChange = {
                config =
                    config.copy(
                        baseUrl = it
                    )
            },
            label = {
                Text("Server Base URL")
            },
            placeholder = {
                Text("http://192.168.x.x:8000")
            }
        )

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = config.healthPath,
            onValueChange = {
                config =
                    config.copy(
                        healthPath = it
                    )
            },
            label = {
                Text("Health endpoint")
            }
        )

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = config.capabilitiesPath,
            onValueChange = {
                config =
                    config.copy(
                        capabilitiesPath = it
                    )
            },
            label = {
                Text("Capabilities endpoint")
            }
        )

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = config.commandPath,
            onValueChange = {
                config =
                    config.copy(
                        commandPath = it
                    )
            },
            label = {
                Text("Command endpoint")
            }
        )

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = config.chatPath,
            onValueChange = {
                config =
                    config.copy(
                        chatPath = it
                    )
            },
            label = {
                Text("Chat endpoint")
            }
        )

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = config.websocketUrl,
            onValueChange = {
                config =
                    config.copy(
                        websocketUrl = it
                    )
            },
            label = {
                Text("WebSocket URL")
            },
            placeholder = {
                Text("ws://server/ws")
            }
        )

        Button(
            modifier =
                Modifier.fillMaxWidth(),
            onClick = {
                store.save(config)

                message =
                    "Configuration saved locally. Endpoints are not verified until real connection testing."
            }
        ) {
            Text(
                "SAVE REAL SERVER CONFIGURATION"
            )
        }

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text(
                modifier =
                    Modifier.padding(12.dp),
                text = message
            )
        }
    }
}
