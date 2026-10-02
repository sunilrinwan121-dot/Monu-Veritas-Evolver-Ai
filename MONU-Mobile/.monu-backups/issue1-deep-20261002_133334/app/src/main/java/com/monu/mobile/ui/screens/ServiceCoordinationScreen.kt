@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServiceCoordinationScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Service Coordination") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "MONU Service Coordinator",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Service authority is not assumed. Only registered and verified services may be coordinated."
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Current state: Service registry awaiting real bindings.")
        }
    }
}
