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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.health.MONUSystemHealthEngine

@Composable
fun SystemHealthScreen() {

    val context = LocalContext.current

    val report =
        MONUSystemHealthEngine(context).inspect()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU System Health")

        Text(
            "Overall Status: ${report.overallStatus}"
        )

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            items(report.metrics) { metric ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {
                        Text(metric.title)
                        Text(metric.description)
                        Text("Status: ${metric.status}")
                        Text("Value: ${metric.value}")
                    }
                }
            }
        }
    }
}
