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
import com.monu.mobile.domain.model.MONUActivityLog
import com.monu.mobile.domain.model.MONUActivitySeverity
import com.monu.mobile.domain.model.MONUActivitySource

@Composable
fun ActivityLogScreen() {

    val activities = listOf(
        MONUActivityLog(
            id = "architecture",
            timestamp = 0L,
            source = MONUActivitySource.SYSTEM,
            severity = MONUActivitySeverity.INFO,
            title = "Live Activity Architecture Ready",
            description = "Real MONU activity events will appear here after server integration."
        ),
        MONUActivityLog(
            id = "transparency",
            timestamp = 0L,
            source = MONUActivitySource.MONU,
            severity = MONUActivitySeverity.INFO,
            title = "Transparency Timeline",
            description = "Commands, planning, assignments and execution events can be recorded here."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Live Activity")

        Text(
            "This timeline is designed for real system transparency."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(activities) { activity ->
                ActivityCard(activity)
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: MONUActivityLog
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(activity.title)
            Text(activity.description)
            Text("Source: ${activity.source}")
            Text("Severity: ${activity.severity}")
        }
    }
}
