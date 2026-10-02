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
import com.monu.mobile.domain.model.MONUAuditAction
import com.monu.mobile.domain.model.MONUAuditActor
import com.monu.mobile.domain.model.MONUAuditEntry
import com.monu.mobile.domain.model.MONUAuditResult

@Composable
fun AuditTrailScreen() {

    val entries = listOf(
        MONUAuditEntry(
            id = "architecture",
            timestamp = 0L,
            actor = MONUAuditActor.SYSTEM,
            action = MONUAuditAction.CONFIGURATION_CHANGED,
            result = MONUAuditResult.UNKNOWN,
            title = "Audit Trail Architecture Ready",
            description = "Real MONU system events can be persistently recorded here after integration."
        ),
        MONUAuditEntry(
            id = "truth",
            timestamp = 0L,
            actor = MONUAuditActor.MONU,
            action = MONUAuditAction.UNKNOWN,
            result = MONUAuditResult.UNKNOWN,
            title = "Transparency Rule",
            description = "Audit entries must eventually originate from real system actions."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Audit Trail")

        Text(
            "A chronological record of important MONU actions."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entry ->
                AuditEntryCard(entry)
            }
        }
    }
}

@Composable
private fun AuditEntryCard(
    entry: MONUAuditEntry
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(entry.title)
            Text(entry.description)
            Text("Actor: ${entry.actor}")
            Text("Action: ${entry.action}")
            Text("Result: ${entry.result}")
        }
    }
}
