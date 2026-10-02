
package com.monu.mobile.feature.recovery

import android.content.Context
import com.monu.mobile.domain.model.RecoveryCheckpoint
import com.monu.mobile.domain.model.RecoveryPlan
import com.monu.mobile.domain.model.RecoveryResult
import com.monu.mobile.domain.model.RecoveryStatus
import com.monu.mobile.feature.backup.MONUBackupCenter
import java.io.File
import java.util.UUID

class MONURecoveryEngine(
    context: Context
) {
    private val appContext = context.applicationContext
    private val backupCenter = MONUBackupCenter(appContext)

    fun checkpoints(): List<RecoveryCheckpoint> {
        return backupCenter.listBackups().map {
            RecoveryCheckpoint(
                id = it.id,
                source = "LOCAL_BACKUP",
                createdAt = it.createdAt ?: 0L,
                verified = it.status ==
                    com.monu.mobile.domain.model.MONUBackupStatus.COMPLETED,
                path = it.filePath,
                checksum = it.checksum
            )
        }
    }

    fun planRecovery(): List<RecoveryPlan> {
        val checkpoint = checkpoints().firstOrNull {
            it.verified && !it.path.isNullOrBlank()
        } ?: return emptyList()

        return listOf(
            RecoveryPlan(
                id = "recovery-${UUID.randomUUID()}",
                target = "MONU Mobile local state",
                checkpointId = checkpoint.id,
                status = RecoveryStatus.RECOVERY_PLANNED,
                steps = listOf(
                    "Verify checkpoint checksum",
                    "Close local database",
                    "Restore shared preferences",
                    "Restore Room database",
                    "Restart application"
                )
            )
        )
    }

    fun recover(plan: RecoveryPlan): RecoveryResult {
        val checkpoint = checkpoints().firstOrNull {
            it.id == plan.checkpointId && it.verified
        }

        if (checkpoint == null || checkpoint.path.isNullOrBlank()) {
            return RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.FAILED,
                evidence = "No verified recovery checkpoint is available."
            )
        }

        val result = backupCenter.restore(
            File(checkpoint.path)
        )

        return if (
            result.status ==
            com.monu.mobile.domain.model.MONUBackupStatus.COMPLETED
        ) {
            RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.RECOVERED,
                evidence =
                    "Recovery completed from ${checkpoint.id}. Restart the application."
            )
        } else {
            RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.FAILED,
                evidence = result.locationDescription
            )
        }
    }
}
