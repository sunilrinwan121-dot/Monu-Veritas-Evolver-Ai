@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntegrationHubScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Integration Hub") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "MONU Integration Hub",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Integration status is displayed only when a real integration is configured or discovered."
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Current state: No verified integrations loaded.")
        }
    }
}
