package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.backup.MONUBackupCenter

@Composable
fun BackupRestoreScreen() {

    val backup = MONUBackupCenter().currentBackup()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Backup & Restore")

        Text("Status: ${backup.status}")
        Text("Location: ${backup.locationDescription}")

        Text("Backup Scope:")

        backup.scopes.forEach { scope ->
            Text("• $scope")
        }

        Button(
            onClick = { }
        ) {
            Text("Create Backup")
        }

        Button(
            onClick = { }
        ) {
            Text("Restore Backup")
        }

        Text(
            "Real backup and restore transport is not configured yet."
        )
    }
}
