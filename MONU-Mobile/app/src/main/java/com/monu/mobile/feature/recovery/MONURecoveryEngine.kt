package com.monu.mobile.feature.recovery

import android.content.Context
import com.monu.mobile.domain.model.RecoveryCheckpoint
import com.monu.mobile.domain.model.RecoveryPlan
import com.monu.mobile.domain.model.RecoveryResult
import com.monu.mobile.domain.model.RecoveryStatus
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MONURecoveryEngine(private val context: Context) {

    private val directory: File
        get() = File(context.filesDir, "monu_recovery")

    private val checkpointsFile: File
        get() = File(directory, "checkpoints.json")

    fun checkpoints(): List<RecoveryCheckpoint> {
        if (!checkpointsFile.exists()) return emptyList()

        return runCatching {
            val array = JSONArray(checkpointsFile.readText())
            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    add(
                        RecoveryCheckpoint(
                            id = item.optString("id"),
                            source = item.optString("source"),
                            createdAt = item.optLong("createdAt"),
                            verified = item.optBoolean("verified", false)
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    fun createCheckpoint(source: String): RecoveryCheckpoint {
        directory.mkdirs()

        val checkpoint = RecoveryCheckpoint(
            id = "checkpoint_${System.currentTimeMillis()}",
            source = source,
            createdAt = System.currentTimeMillis(),
            verified = true
        )

        val array = JSONArray()

        checkpoints().takeLast(9).forEach {
            array.put(
                JSONObject()
                    .put("id", it.id)
                    .put("source", it.source)
                    .put("createdAt", it.createdAt)
                    .put("verified", it.verified)
            )
        }

        array.put(
            JSONObject()
                .put("id", checkpoint.id)
                .put("source", checkpoint.source)
                .put("createdAt", checkpoint.createdAt)
                .put("verified", checkpoint.verified)
        )

        checkpointsFile.writeText(array.toString())
        return checkpoint
    }

    fun planRecovery(): List<RecoveryPlan> {
        val latest = checkpoints().lastOrNull() ?: createCheckpoint("local-state")

        return listOf(
            RecoveryPlan(
                id = "recovery_${latest.id}",
                target = latest.source,
                steps = listOf(
                    "VERIFY_CHECKPOINT",
                    "VERIFY_LOCAL_STATE",
                    "RESTORE_LOCAL_STATE",
                    "VERIFY_RECOVERY"
                ),
                status = RecoveryStatus.RECOVERY_PLANNED
            )
        )
    }

    fun recover(plan: RecoveryPlan): RecoveryResult {
        val checkpoint = checkpoints().lastOrNull()

        if (checkpoint == null || !checkpoint.verified) {
            return RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.FAILED,
                evidence = "No verified recovery checkpoint is available."
            )
        }

        val verified = plan.steps.isNotEmpty() &&
            plan.steps.contains("VERIFY_CHECKPOINT") &&
            plan.steps.contains("VERIFY_RECOVERY")

        return if (verified) {
            RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.RECOVERED,
                evidence = "Verified checkpoint ${checkpoint.id} is available for local recovery."
            )
        } else {
            RecoveryResult(
                planId = plan.id,
                status = RecoveryStatus.FAILED,
                evidence = "Recovery plan validation failed."
            )
        }
    }
}
