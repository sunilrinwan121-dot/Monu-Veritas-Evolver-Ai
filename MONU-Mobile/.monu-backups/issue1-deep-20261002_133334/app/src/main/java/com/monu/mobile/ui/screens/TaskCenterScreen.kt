package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.MONUTask
import com.monu.mobile.domain.model.MONUTaskPriority
import com.monu.mobile.domain.model.MONUTaskStatus

@Composable
fun TaskCenterScreen() {

    val tasks = listOf(
        MONUTask(
            id = "task-1",
            title = "MONU Task Center",
            description = "Task lifecycle architecture initialized",
            status = MONUTaskStatus.RUNNING,
            priority = MONUTaskPriority.NORMAL,
            progress = 45,
            currentStage = "Building mobile architecture"
        ),
        MONUTask(
            id = "task-2",
            title = "Server Integration",
            description = "Waiting for real endpoint configuration",
            status = MONUTaskStatus.QUEUED,
            priority = MONUTaskPriority.HIGH,
            progress = 0,
            currentStage = "Waiting"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Live Task Center")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(tasks) { task ->
                TaskCard(task)
            }
        }
    }
}

@Composable
private fun TaskCard(task: MONUTask) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(task.title)
            Text(task.description)
            Text("Status: ${task.status}")
            Text("Stage: ${task.currentStage}")
            Text("Progress: ${task.progress}%")

            LinearProgressIndicator(
                progress = { task.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
