package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransferCenterScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("MONU Transfer Center")

        TransferInfoCard(
            "Advanced Upload",
            "Chunk architecture, pause, resume and retry support prepared."
        )

        TransferInfoCard(
            "Smart Download",
            "Background download architecture and progress tracking prepared."
        )

        TransferInfoCard(
            "Large Files",
            "Designed for large videos, documents, archives and project files."
        )

        TransferInfoCard(
            "Transfer Truth",
            "A transfer is never marked COMPLETED until a real transport implementation confirms completion."
        )

        TransferInfoCard(
            "Server Integration",
            "Real upload and download endpoints are required before network transfer execution."
        )
    }
}

@Composable
private fun TransferInfoCard(
    title: String,
    description: String
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(title)
            Text(description)
        }
    }
}
