package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.workflows.MONUWorkflowCenter

@Composable
fun WorkflowAutomationScreen() {
    val context = LocalContext.current
    val center = remember(context) { MONUWorkflowCenter(context) }
    val workflows = remember { center.workflows() }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("MONU Workflow Automation")

        if (result.isNotBlank()) {
            Text(result)
        }

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(workflows) { workflow ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(workflow.name)
                        Text(workflow.description)
                        Text("Status: ${workflow.status}")
                        Text("Trigger: ${workflow.trigger}")
                        Text("Steps: ${workflow.steps.size}")

                        Button(
                            onClick = {
                                val run = center.createRun(workflow)
                                result = "${workflow.name}: ${run.status} - ${run.message.orEmpty()}"
                            }
                        ) {
                            Text("Run Workflow")
                        }
                    }
                }
            }
        }
    }
}
