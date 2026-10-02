package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.offline.OfflineCommandQueue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OfflineQueueScreen() {

    val context =
        LocalContext.current

    val scope =
        rememberCoroutineScope()

    val queue =
        remember {
            OfflineCommandQueue(context)
        }

    var command by remember {
        mutableStateOf("")
    }

    var queueCount by remember {
        mutableStateOf(0)
    }

    var message by remember {
        mutableStateOf(
            "Local queue ready"
        )
    }

    fun refreshCount() {
        scope.launch {
            queueCount =
                withContext(Dispatchers.IO) {
                    queue.count()
                }
        }
    }

    LaunchedEffect(Unit) {
        refreshCount()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "OFFLINE COMMAND QUEUE",
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
                    text =
                        "Pending local commands: $queueCount"
                )

                Text(
                    text = message
                )
            }
        }

        OutlinedTextField(
            modifier =
                Modifier.fillMaxWidth(),
            value = command,
            onValueChange = {
                command = it
            },
            label = {
                Text(
                    "Test offline command"
                )
            }
        )

        Button(
            enabled =
                command.isNotBlank(),
            onClick = {

                val value =
                    command.trim()

                scope.launch {

                    withContext(
                        Dispatchers.IO
                    ) {
                        queue.enqueue(value)
                    }

                    command = ""

                    message =
                        "Command stored locally"

                    refreshCount()
                }
            }
        ) {
            Text(
                "STORE IN LOCAL QUEUE"
            )
        }

        Button(
            onClick = {
                refreshCount()
                message =
                    "Queue refreshed"
            }
        ) {
            Text(
                "REFRESH QUEUE"
            )
        }
    }
}
