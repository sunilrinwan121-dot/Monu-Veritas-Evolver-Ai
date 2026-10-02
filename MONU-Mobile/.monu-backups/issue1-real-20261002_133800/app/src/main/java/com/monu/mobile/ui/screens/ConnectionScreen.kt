package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.ConnectionState
import com.monu.mobile.domain.model.ConnectionStatus
import com.monu.mobile.domain.repository.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConnectionScreen() {

    val scope = rememberCoroutineScope()

    var status by remember {
        mutableStateOf(
            ConnectionStatus()
        )
    }

    var checking by remember {
        mutableStateOf(false)
    }

    fun checkConnection() {
        scope.launch {

            checking = true

            status = withContext(Dispatchers.IO) {
                ConnectionRepository()
                    .checkConnection()
            }

            checking = false
        }
    }

    LaunchedEffect(Unit) {
        checkConnection()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {

        Text(
            text = "SERVER CONNECTION TRUTH",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Status is based on real network requests only.",
            style = MaterialTheme.typography.bodyMedium
        )

        ConnectionCard(
            title = "APK → MONU SERVER",
            state = status.apkToServer
        )

        ConnectionCard(
            title = "MONU SERVER → APK",
            state = status.serverToApk
        )

        Text(
            text = "Message: ${status.message}"
        )

        Text(
            text = "Latency: ${
                status.latencyMs?.let { "$it ms" }
                    ?: "Not available"
            }"
        )

        Text(
            text = "Last Check: ${
                status.lastCheckedAt?.let {
                    SimpleDateFormat(
                        "HH:mm:ss",
                        Locale.getDefault()
                    ).format(Date(it))
                } ?: "Never"
            }"
        )

        Button(
            enabled = !checking,
            onClick = {
                checkConnection()
            }
        ) {
            Text(
                if (checking)
                    "CHECKING..."
                else
                    "CHECK CONNECTION NOW"
            )
        }
    }
}

@Composable
private fun ConnectionCard(
    title: String,
    state: ConnectionState
) {

    val indicatorColor =
        when (state) {
            ConnectionState.CONNECTED ->
                Color(0xFF2E7D32)

            ConnectionState.DISCONNECTED ->
                Color(0xFFC62828)

            ConnectionState.CHECKING ->
                Color(0xFFFFA000)

            ConnectionState.NOT_CONFIGURED ->
                Color(0xFF757575)

            ConnectionState.UNKNOWN ->
                Color(0xFF757575)
        }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "●",
                color = indicatorColor,
                style =
                    MaterialTheme.typography.headlineMedium
            )

            Column {
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Text(
                    text = state.name
                )
            }
        }
    }
}
