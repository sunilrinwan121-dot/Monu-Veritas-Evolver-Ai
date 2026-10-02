
package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.recovery.MONURecoveryEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SystemRecoveryScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val engine = remember(context) {
        MONURecoveryEngine(context)
    }

    var checkpoints by remember {
        mutableStateOf(engine.checkpoints())
    }
    var plans by remember {
        mutableStateOf(engine.planRecovery())
    }
    var message by remember {
        mutableStateOf("")
    }
    var busy by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("System Recovery")
        Spacer(Modifier.height(8.dp))
        Text("Recovery uses verified local backup checkpoints.")

        Spacer(Modifier.height(12.dp))

        Text("Verified Checkpoints: ${checkpoints.count { it.verified }}")

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(checkpoints, key = { it.id }) { checkpoint ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(checkpoint.id)
                        Text("Source: ${checkpoint.source}")
                        Text("Verified: ${checkpoint.verified}")
                        Text("Created: ${checkpoint.createdAt}")
                    }
                }
            }
        }

        if (plans.isNotEmpty()) {
            Button(
                enabled = !busy,
                onClick = {
                    busy = true
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            engine.recover(plans.first())
                        }
                        message = result.evidence ?: result.status.name
                        checkpoints = engine.checkpoints()
                        plans = engine.planRecovery()
                        busy = false
                    }
                }
            ) {
                Text("Recover From Latest Checkpoint")
            }
        }

        if (message.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(message)
        }

        Spacer(Modifier.height(8.dp))
        Text("A successful restore requires an application restart.")
    }
}
