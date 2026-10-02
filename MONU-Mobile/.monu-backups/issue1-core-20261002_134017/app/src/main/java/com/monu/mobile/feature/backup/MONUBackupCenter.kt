package com.monu.mobile.feature.backup

import com.monu.mobile.domain.model.MONUBackupInfo
import com.monu.mobile.domain.model.MONUBackupScope
import com.monu.mobile.domain.model.MONUBackupStatus

class MONUBackupCenter {

    fun currentBackup(): MONUBackupInfo {
        return MONUBackupInfo(
            id = "local-backup-status",
            createdAt = null,
            status = MONUBackupStatus.NEVER_CREATED,
            scopes = listOf(
                MONUBackupScope.SETTINGS,
                MONUBackupScope.OFFLINE_COMMANDS,
                MONUBackupScope.LOCAL_DATABASE,
                MONUBackupScope.PROJECT_METADATA
            ),
            locationDescription =
                "No real backup location configured yet."
        )
    }

    fun backupScopes(): List<MONUBackupScope> {
        return currentBackup().scopes
    }
}
