package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.diagnostics.MONUSelfDiagnostics

@Composable
fun SelfDiagnosticsScreen() {

    val context = LocalContext.current

    var reportText by remember {
        mutableStateOf("Diagnostics not yet executed.")
    }

    var results by remember {
        mutableStateOf(
            emptyList<com.monu.mobile.domain.model.MONUDiagnosticResult>()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU APK Self-Diagnostics")

        Button(
            onClick = {

                val report =
                    MONUSelfDiagnostics(context)
                        .runDiagnostics()

                results = report.results

                reportText =
                    "Diagnostics completed: ${results.size} checks"
            }
        ) {
            Text("Run Real Diagnostics")
        }

        Text(reportText)

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(results) { result ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(result.title)
                        Text(result.description)
                        Text("Category: ${result.category}")
                        Text("Status: ${result.status}")
                    }
                }
            }
        }
    }
}
