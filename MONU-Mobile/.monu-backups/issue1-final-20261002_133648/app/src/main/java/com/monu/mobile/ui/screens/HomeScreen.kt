package com.monu.mobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MONUFeatureCard(
    val title: String,
    val description: String,
    val icon: String
)

@Composable
fun HomeScreen(
    onOpenCommand: () -> Unit
) {
    val features = listOf(
        MONUFeatureCard("Command Center", "Text, voice and multimodal commands", "◉"),
        MONUFeatureCard("Server", "Real connection and capability status", "◆"),
        MONUFeatureCard("Tasks", "Monitor long-running operations", "✓"),
        MONUFeatureCard("Projects", "Projects, goals and activity", "▣"),
        MONUFeatureCard("Media Studio", "Image, video and audio operations", "◈"),
        MONUFeatureCard("Files", "Upload, download and file operations", "▤"),
        MONUFeatureCard("Voice", "Voice commands and spoken responses", "◌"),
        MONUFeatureCard("Security", "Permissions and device protection", "◈"),
        MONUFeatureCard("Device Intelligence", "Mobile health and diagnostics", "▣"),
        MONUFeatureCard("Activity", "Live MONU activity feed", "≡")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "MONU COMMAND CENTER",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Your personal AI operating interface",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpenCommand
            ) {
                Text("OPEN UNIVERSAL COMMAND")
            }
        }

        items(features.size) { index ->
            val feature = features[index]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (feature.title == "Command Center") {
                            onOpenCommand()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = feature.icon,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = feature.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = feature.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
