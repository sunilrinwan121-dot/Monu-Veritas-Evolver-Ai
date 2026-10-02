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
import com.monu.mobile.domain.model.MONUProject
import com.monu.mobile.domain.model.MONUProjectStatus

@Composable
fun ProjectCenterScreen() {

    val projects = listOf(
        MONUProject(
            id = "monu-mobile",
            name = "MONU Mobile Command OS",
            description = "Android owner command center",
            status = MONUProjectStatus.ACTIVE,
            progress = 35
        ),
        MONUProject(
            id = "monu-server",
            name = "MONU Server",
            description = "Central AI brain and workforce",
            status = MONUProjectStatus.ACTIVE,
            progress = 70
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Project Command Center")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(projects) { project ->
                ProjectCard(project)
            }
        }
    }
}

@Composable
private fun ProjectCard(project: MONUProject) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(project.name)
            Text(project.description)
            Text("Status: ${project.status}")
            Text("Progress: ${project.progress}%")

            LinearProgressIndicator(
                progress = { project.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                "Goals • Tasks • Files • Employees • Reports",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
