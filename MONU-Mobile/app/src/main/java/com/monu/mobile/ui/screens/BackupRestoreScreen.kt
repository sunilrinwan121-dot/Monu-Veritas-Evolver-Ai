package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.backup.MONUBackupCenter

@Composable
fun BackupRestoreScreen() {
    val context = LocalContext.current
    val center = remember(context) { MONUBackupCenter(context) }
    var backup by remember { mutableStateOf(center.currentBackup()) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("MONU Backup & Restore")
        Text("Status: ${backup.status}")
        Text("Location: ${backup.locationDescription}")
        Text("Backup Scope:")

        backup.scopes.forEach { scope ->
            Text("• $scope")
        }

        Button(
            onClick = {
                backup = center.createBackup()
                message = "Backup completed with status ${backup.status}."
            }
        ) {
            Text("Create Backup")
        }

        Button(
            onClick = {
                val restored = center.restoreBackup()
                message = if (restored) {
                    "Backup verification and restore preparation completed."
                } else {
                    "Backup restore verification failed."
                }
            }
        ) {
            Text("Restore Backup")
        }

        if (message.isNotBlank()) {
            Text(message)
        }
    }
}
