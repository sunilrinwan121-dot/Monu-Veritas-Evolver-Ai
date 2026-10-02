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
import com.monu.mobile.feature.verification.MONUVerificationEngine

@Composable
fun VerificationEngineScreen() {

    val engine = remember {
        MONUVerificationEngine()
    }

    val report = engine.report()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Verification Engine")
        Text("Total: ${report.total}")
        Text("Verified: ${report.verified}")
        Text("Rejected: ${report.rejected}")
        Text("Inconclusive: ${report.inconclusive}")
        Text("Unknown: ${report.unknown}")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(report.results) { result ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(result.message)
                        Text("Status: ${result.status}")
                        Text("Execution: ${result.executionId}")
                        Text("Evidence: ${result.evidence.size}")

                        result.evidence.forEach {
                            Text(
                                "${it.type}: ${it.description}"
                            )
                        }
                    }
                }
            }
        }
    }
}
