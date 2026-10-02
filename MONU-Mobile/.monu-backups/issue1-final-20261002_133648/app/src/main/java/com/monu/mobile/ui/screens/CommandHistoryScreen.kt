package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.MONUCommandHistoryEntry
import com.monu.mobile.domain.model.MONUCommandStatus

@Composable
fun CommandHistoryScreen() {

    val entries = listOf(
        MONUCommandHistoryEntry(
            id = "architecture",
            command = "Command history architecture",
            timestamp = 0L,
            status = MONUCommandStatus.UNKNOWN,
            resultSummary = "Real command events will appear here after integration.",
            verified = false
        ),
        MONUCommandHistoryEntry(
            id = "intelligence",
            command = "Command pattern intelligence",
            timestamp = 0L,
            status = MONUCommandStatus.UNKNOWN,
            resultSummary = "Patterns will be calculated from real recorded commands.",
            verified = false
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Command History")

        Text(
            "Command history and intelligence must be based on real recorded commands."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entry ->
                CommandHistoryCard(entry)
            }
        }
    }
}

@Composable
private fun CommandHistoryCard(
    entry: MONUCommandHistoryEntry
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(entry.command)
            Text(entry.resultSummary ?: "No result summary")
            Text("Status: ${entry.status}")
            Text("Verified: ${entry.verified}")
        }
    }
}
