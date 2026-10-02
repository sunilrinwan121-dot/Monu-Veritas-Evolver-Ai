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
import com.monu.mobile.domain.model.MONUDecision
import com.monu.mobile.domain.model.MONUDecisionStatus

@Composable
fun DecisionCenterScreen() {

    val decisions = listOf(
        MONUDecision(
            id = "decision_architecture",
            title = "Decision Architecture",
            description = "Real decisions will appear when MONU receives verified decision inputs.",
            status = MONUDecisionStatus.UNKNOWN,
            source = "LOCAL_ARCHITECTURE"
        ),
        MONUDecision(
            id = "decision_truth",
            title = "Decision Transparency",
            description = "Decision options should eventually expose their real factors and sources.",
            status = MONUDecisionStatus.UNKNOWN,
            source = "LOCAL_ARCHITECTURE"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Decision Center")

        Text(
            "Decision architecture for evaluating options without fabricating conclusions."
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(decisions) { decision ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(decision.title)
                        Text(decision.description)
                        Text("Status: ${decision.status}")
                        Text("Source: ${decision.source}")
                    }
                }
            }
        }
    }
}
