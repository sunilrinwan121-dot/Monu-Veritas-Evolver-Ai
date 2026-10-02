package com.monu.mobile.feature.recovery

import com.monu.mobile.domain.model.RecoveryCheckpoint
import com.monu.mobile.domain.model.RecoveryPlan
import com.monu.mobile.domain.model.RecoveryResult
import com.monu.mobile.domain.model.RecoveryStatus

class MONURecoveryEngine {

    private val checkpointStore = mutableListOf<RecoveryCheckpoint>()

    fun checkpoints(): List<RecoveryCheckpoint> {
        return checkpointStore.toList()
    }

    fun planRecovery(): List<RecoveryPlan> {
        val checkpoints = checkpoints()

        if (checkpoints.isEmpty()) {
            return emptyList()
        }

        return checkpoints
            .filter { it.verified }
            .map { checkpoint ->
                RecoveryPlan(
                    id = "recovery-plan-${checkpoint.id}",
                    target = checkpoint.source,
                    steps = listOf(
                        "Validate checkpoint ${checkpoint.id}",
                        "Verify checkpoint integrity",
                        "Restore verified checkpoint state"
                    ),
                    status = RecoveryStatus.RECOVERY_PLANNED
                )
            }
    }

    fun createCheckpoint(
        source: String,
        verified: Boolean = true
    ): RecoveryCheckpoint {
        require(source.isNotBlank()) {
            "Checkpoint source must not be blank."
        }

        val checkpoint = RecoveryCheckpoint(
            id = "checkpoint-${System.currentTimeMillis()}",
            source = source,
            createdAt = System.currentTimeMillis(),
            verified = verified
        )

        checkpointStore.add(checkpoint)
        return checkpoint
    }

    fun recover(
        plan: RecoveryPlan
    ): RecoveryResult {
        val checkpoint = checkpointStore.firstOrNull {
            it.source == plan.target && it.verified
        }

        return if (checkpoint != null) {
            RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.RECOVERED,
                evidence = "Verified checkpoint ${checkpoint.id} is available for recovery."
            )
        } else {
            RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.FAILED,
                evidence = "No verified checkpoint is available for target ${plan.target}."
            )
        }
    }
}
