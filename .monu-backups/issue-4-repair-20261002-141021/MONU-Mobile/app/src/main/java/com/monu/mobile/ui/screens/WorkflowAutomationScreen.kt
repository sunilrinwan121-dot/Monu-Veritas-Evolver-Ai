
package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.workflows.MONUWorkflowCenter
import kotlinx.coroutines.launch

@Composable
fun WorkflowAutomationScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val center = remember(context) {
        MONUWorkflowCenter(context)
    }

    var workflows by remember { mutableStateOf(center.workflows()) }
    var message by remember { mutableStateOf("") }
    var runningId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("MONU Workflow Automation")
        Spacer(Modifier.height(8.dp))
        Text("Real local workflow execution with persistent state and scheduled execution.")

        if (message.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(message)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(workflows, key = { it.id }) { workflow ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(workflow.name)
                        Text(workflow.description)
                        Text("Status: ${workflow.status}")
                        Text("Trigger: ${workflow.trigger}")
                        Text("Steps: ${workflow.steps.size}")

                        workflow.lastRunTimestamp?.let {
                            Text("Last run: $it")
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            enabled = runningId == null,
                            onClick = {
                                runningId = workflow.id
                                scope.launch {
                                    val result = center.execute(workflow.id)
                                    message =
                                        result.message ?: result.status.name
                                    workflows = center.workflows()
                                    runningId = null
                                }
                            }
                        ) {
                            Text(
                                if (runningId == workflow.id) {
                                    "Running..."
                                } else {
                                    "Run Now"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
