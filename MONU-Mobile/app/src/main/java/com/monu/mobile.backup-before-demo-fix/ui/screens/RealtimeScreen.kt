package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.data.config.ServerConfigStore
import com.monu.mobile.domain.model.WebSocketState
import com.monu.mobile.feature.realtime.MONUWebSocketEngine

@Composable
fun RealtimeScreen() {

    val context =
        LocalContext.current

    val store =
        remember {
            ServerConfigStore(context)
        }

    val engine =
        remember {
            MONUWebSocketEngine()
        }

    val status by
        engine.status.collectAsState()

    val websocketUrl =
        remember {
            store.load().websocketUrl
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "REAL-TIME CONNECTION",
            style =
                MaterialTheme.typography
                    .headlineSmall
        )

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Text(
                    "State: ${status.state}"
                )

                Text(
                    "Status: ${status.message}"
                )

                Text(
                    "WebSocket URL: " +
                        if (
                            websocketUrl.isBlank()
                        ) {
                            "NOT CONFIGURED"
                        } else {
                            websocketUrl
                        }
                )
            }
        }

        Button(
            modifier =
                Modifier.fillMaxWidth(),
            enabled =
                websocketUrl.isNotBlank() &&
                    status.state !=
                    WebSocketState.CONNECTING,
            onClick = {
                engine.connect(
                    websocketUrl
                )
            }
        ) {
            Text("CONNECT REAL WEBSOCKET")
        }

        OutlinedButton(
            modifier =
                Modifier.fillMaxWidth(),
            onClick = {
                engine.disconnect()
            }
        ) {
            Text("DISCONNECT")
        }

        Text(
            "Truth Rule: CONNECTED appears only after WebSocket onOpen()."
        )
    }
}
