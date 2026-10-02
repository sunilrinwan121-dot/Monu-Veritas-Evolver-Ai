
package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.feature.backup.MONUBackupCenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BackupRestoreScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val center = remember(context) {
        MONUBackupCenter(context)
    }

    var backup by remember {
        mutableStateOf(center.currentBackup())
    }
    var message by remember {
        mutableStateOf("")
    }
    var busy by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("MONU Backup & Restore")
        Text("Status: ${backup.status}")
        Text("Location: ${backup.locationDescription}")
        Text("Size: ${backup.sizeBytes} bytes")

        if (!backup.checksum.isNullOrBlank()) {
            Text("SHA-256: ${backup.checksum}")
        }

        Text("Backup Scope:")
        backup.scopes.forEach {
            Text("• $it")
        }

        Button(
            enabled = !busy,
            onClick = {
                busy = true
                scope.launch {
                    backup = withContext(Dispatchers.IO) {
                        center.createBackup()
                    }
                    message = backup.locationDescription
                    busy = false
                }
            }
        ) {
            Text("Create Verified Backup")
        }

        Button(
            enabled = !busy && backup.filePath != null,
            onClick = {
                busy = true
                scope.launch {
                    backup = withContext(Dispatchers.IO) {
                        center.restoreLatest()
                    }
                    message = backup.locationDescription
                    busy = false
                }
            }
        ) {
            Text("Restore Latest Backup")
        }

        if (message.isNotBlank()) {
            Text(message)
        }

        Text(
            "Restore replaces local application state and requires an application restart."
        )
    }
}
