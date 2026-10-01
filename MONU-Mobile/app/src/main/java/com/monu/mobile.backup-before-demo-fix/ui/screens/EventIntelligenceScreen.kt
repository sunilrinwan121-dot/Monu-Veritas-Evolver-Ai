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
import com.monu.mobile.feature.events.MONUEventIntelligenceHub

@Composable
fun EventIntelligenceScreen() {

    val hub = remember {
        MONUEventIntelligenceHub()
    }

    val report = hub.report()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Event Intelligence Hub")
        Text("Total: ${report.total}")
        Text("Received: ${report.received}")
        Text("Processed: ${report.processed}")
        Text("Failed: ${report.failed}")
        Text("Unknown: ${report.unknown}")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(report.events) { event ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(event.title)
                        Text("Type: ${event.type}")
                        Text("Status: ${event.status}")
                        Text("Source: ${event.source}")
                    }
                }
            }
        }
    }
}
