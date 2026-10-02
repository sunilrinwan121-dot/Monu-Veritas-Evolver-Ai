package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.recovery.MONURecoveryEngine

@Composable
fun SystemRecoveryScreen() {
    val context = LocalContext.current
    val engine = remember(context) { MONURecoveryEngine(context) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("System Recovery")
        Text("Local recovery checkpoints and verified recovery plans are available.")

        Button(
            onClick = {
                val checkpoint = engine.createCheckpoint("local-state")
                message = "Checkpoint created: ${checkpoint.id}"
            }
        ) {
            Text("Create Checkpoint")
        }

        Button(
            onClick = {
                val plan = engine.planRecovery().firstOrNull()
                message = if (plan == null) {
                    "No recovery plan is available."
                } else {
                    "Recovery plan ready: ${plan.id}"
                }
            }
        ) {
            Text("Plan Recovery")
        }

        Button(
            onClick = {
                val plan = engine.planRecovery().firstOrNull()
                message = if (plan == null) {
                    "No recovery plan is available."
                } else {
                    val result = engine.recover(plan)
                    "${result.status}: ${result.evidence.orEmpty()}"
                }
            }
        ) {
            Text("Run Recovery")
        }

        if (message.isNotBlank()) {
            Text(message)
        }
    }
}
