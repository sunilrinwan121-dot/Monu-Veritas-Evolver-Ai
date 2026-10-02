package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.execution.MONUExecutionOrchestrator

@Composable
fun ExecutionOrchestratorScreen() {

    val orchestrator = remember {
        MONUExecutionOrchestrator()
    }

    val report = orchestrator.report()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Execution Orchestrator")
        Text("Total: ${report.total}")
        Text("Running: ${report.running}")
        Text("Succeeded: ${report.succeeded}")
        Text("Failed: ${report.failed}")
        Text("Unknown: ${report.unknown}")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(report.executions) { execution ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(execution.title)
                        Text("Type: ${execution.type}")
                        Text("Status: ${execution.status}")
                        Text("ID: ${execution.executionId}")

                        execution.error?.let {
                            Text("Error: $it")
                        }
                    }
                }
            }
        }
    }
}
