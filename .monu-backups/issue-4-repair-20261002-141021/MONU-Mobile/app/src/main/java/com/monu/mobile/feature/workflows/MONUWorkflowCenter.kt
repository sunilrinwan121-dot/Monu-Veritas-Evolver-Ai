
package com.monu.mobile.feature.workflows

import android.content.Context
import com.monu.mobile.data.local.MONUDatabaseProvider
import com.monu.mobile.data.network.MONUServerClient
import com.monu.mobile.domain.model.MONUWorkflow
import com.monu.mobile.domain.model.MONUWorkflowAction
import com.monu.mobile.domain.model.MONUWorkflowRun
import com.monu.mobile.domain.model.MONUWorkflowStatus
import com.monu.mobile.feature.backup.MONUBackupCenter
import com.monu.mobile.feature.memory.MONUMemoryEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class MONUWorkflowCenter(
    context: Context
) {
    private val appContext = context.applicationContext
    private val preferences =
        appContext.getSharedPreferences(
            "monu_workflows",
            Context.MODE_PRIVATE
        )

    fun workflows(): List<MONUWorkflow> {
        val raw = preferences.getString("workflows_v1", null)

        if (raw.isNullOrBlank()) {
            val defaults = defaultWorkflows()
            saveWorkflows(defaults)
            return defaults
        }

        return runCatching {
            val array = JSONArray(raw)
            List(array.length()) { index ->
                fromJson(array.getJSONObject(index))
            }
        }.getOrElse {
            val defaults = defaultWorkflows()
            saveWorkflows(defaults)
            defaults
        }
    }

    fun demoWorkflows(): List<MONUWorkflow> = workflows()

    suspend fun execute(workflowId: String): MONUWorkflowRun =
        withContext(Dispatchers.IO) {
            val workflow = workflows().firstOrNull { it.id == workflowId }
                ?: return@withContext MONUWorkflowRun(
                    id = UUID.randomUUID().toString(),
                    workflowId = workflowId,
                    status = MONUWorkflowStatus.FAILED,
                    startedAt = System.currentTimeMillis(),
                    completedAt = System.currentTimeMillis(),
                    message = "Workflow not found."
                )

            val start = System.currentTimeMillis()
            val runId = "run-${UUID.randomUUID()}"

            updateWorkflowStatus(
                workflow.id,
                MONUWorkflowStatus.RUNNING,
                null
            )

            val evidence = mutableListOf<String>()
            var failed = false

            try {
                workflow.steps
                    .filter { it.enabled }
                    .sortedBy { it.order }
                    .forEach { step ->
                        when (step.action) {
                            MONUWorkflowAction.HEALTH_CHECK -> {
                                val result = MONUServerClient().checkHealth()
                                evidence += "HEALTH_CHECK: ${result.message}"
                                if (result.apkToServer.name == "DISCONNECTED") {
                                    failed = true
                                }
                            }

                            MONUWorkflowAction.DATABASE_CHECK -> {
                                val count =
                                    MONUDatabaseProvider
                                        .get(appContext)
                                        .offlineCommandDao()
                                        .count()
                                evidence += "DATABASE_CHECK: offlineCommands=$count"
                            }

                            MONUWorkflowAction.MEMORY_CHECK -> {
                                val count = MONUMemoryEngine(appContext).all().size
                                evidence += "MEMORY_CHECK: memories=$count"
                            }

                            MONUWorkflowAction.BACKUP -> {
                                val backup = MONUBackupCenter(appContext).createBackup()
                                evidence += "BACKUP: ${backup.status} ${backup.locationDescription}"
                                if (backup.status != com.monu.mobile.domain.model.MONUBackupStatus.COMPLETED) {
                                    failed = true
                                }
                            }

                            MONUWorkflowAction.NO_OP -> {
                                evidence += "NO_OP: completed"
                            }
                        }
                    }

                val status =
                    if (failed) {
                        MONUWorkflowStatus.FAILED
                    } else {
                        MONUWorkflowStatus.COMPLETED
                    }

                updateWorkflowStatus(
                    workflow.id,
                    status,
                    System.currentTimeMillis()
                )

                MONUWorkflowRun(
                    id = runId,
                    workflowId = workflow.id,
                    status = status,
                    startedAt = start,
                    completedAt = System.currentTimeMillis(),
                    message = evidence.joinToString("\n")
                )
            } catch (error: Throwable) {
                updateWorkflowStatus(
                    workflow.id,
                    MONUWorkflowStatus.FAILED,
                    System.currentTimeMillis()
                )

                MONUWorkflowRun(
                    id = runId,
                    workflowId = workflow.id,
                    status = MONUWorkflowStatus.FAILED,
                    startedAt = start,
                    completedAt = System.currentTimeMillis(),
                    message = error.message ?: "Workflow execution failed."
                )
            }
        }

    private fun defaultWorkflows(): List<MONUWorkflow> {
        return listOf(
            MONUWorkflow(
                id = "daily_system_review",
                name = "Daily System Review",
                description = "Runs real local database, memory and server health checks.",
                status = MONUWorkflowStatus.ENABLED,
                trigger = com.monu.mobile.domain.model.MONUWorkflowTriggerType.SCHEDULE,
                scheduleMinutes = 1440L,
                steps = listOf(
                    MONUWorkflowStep(
                        id = "health",
                        title = "Server health",
                        action = MONUWorkflowAction.HEALTH_CHECK,
                        order = 1
                    ),
                    MONUWorkflowStep(
                        id = "database",
                        title = "Local database check",
                        action = MONUWorkflowAction.DATABASE_CHECK,
                        order = 2
                    ),
                    MONUWorkflowStep(
                        id = "memory",
                        title = "Memory check",
                        action = MONUWorkflowAction.MEMORY_CHECK,
                        order = 3
                    )
                )
            ),
            MONUWorkflow(
                id = "verified_local_backup",
                name = "Verified Local Backup",
                description = "Creates a real verified local backup of APK state.",
                status = MONUWorkflowStatus.ENABLED,
                trigger = com.monu.mobile.domain.model.MONUWorkflowTriggerType.MANUAL,
                steps = listOf(
                    MONUWorkflowStep(
                        id = "backup",
                        title = "Create backup",
                        action = MONUWorkflowAction.BACKUP,
                        order = 1
                    )
                )
            )
        )
    }

    private fun updateWorkflowStatus(
        workflowId: String,
        status: MONUWorkflowStatus,
        timestamp: Long?
    ) {
        val updated = workflows().map {
            if (it.id == workflowId) {
                it.copy(
                    status = status,
                    lastRunTimestamp = timestamp
                )
            } else {
                it
            }
        }
        saveWorkflows(updated)
    }

    private fun saveWorkflows(workflows: List<MONUWorkflow>) {
        val array = JSONArray()
        workflows.forEach {
            array.put(toJson(it))
        }

        preferences.edit()
            .putString("workflows_v1", array.toString())
            .apply()
    }

    private fun toJson(workflow: MONUWorkflow): JSONObject {
        val steps = JSONArray()

        workflow.steps.forEach {
            steps.put(
                JSONObject()
                    .put("id", it.id)
                    .put("title", it.title)
                    .put("action", it.action.name)
                    .put("order", it.order)
                    .put("enabled", it.enabled)
            )
        }

        return JSONObject()
            .put("id", workflow.id)
            .put("name", workflow.name)
            .put("description", workflow.description)
            .put("status", workflow.status.name)
            .put("trigger", workflow.trigger.name)
            .put("lastRunTimestamp", workflow.lastRunTimestamp ?: JSONObject.NULL)
            .put("scheduleMinutes", workflow.scheduleMinutes ?: JSONObject.NULL)
            .put("steps", steps)
    }

    private fun fromJson(json: JSONObject): MONUWorkflow {
        val stepsJson = json.optJSONArray("steps") ?: JSONArray()
        val steps = List(stepsJson.length()) { index ->
            val step = stepsJson.getJSONObject(index)
            MONUWorkflowStep(
                id = step.getString("id"),
                title = step.getString("title"),
                action = runCatching {
                    MONUWorkflowAction.valueOf(
                        step.optString(
                            "action",
                            MONUWorkflowAction.NO_OP.name
                        )
                    )
                }.getOrDefault(MONUWorkflowAction.NO_OP),
                order = step.optInt("order", index + 1),
                enabled = step.optBoolean("enabled", true)
            )
        }

        return MONUWorkflow(
            id = json.getString("id"),
            name = json.getString("name"),
            description = json.getString("description"),
            status = runCatching {
                MONUWorkflowStatus.valueOf(
                    json.optString(
                        "status",
                        MONUWorkflowStatus.DRAFT.name
                    )
                )
            }.getOrDefault(MONUWorkflowStatus.DRAFT),
            trigger = runCatching {
                com.monu.mobile.domain.model.MONUWorkflowTriggerType.valueOf(
                    json.optString(
                        "trigger",
                        com.monu.mobile.domain.model.MONUWorkflowTriggerType.MANUAL.name
                    )
                )
            }.getOrDefault(
                com.monu.mobile.domain.model.MONUWorkflowTriggerType.MANUAL
            ),
            steps = steps,
            lastRunTimestamp =
                if (json.isNull("lastRunTimestamp")) null
                else json.optLong("lastRunTimestamp"),
            scheduleMinutes =
                if (json.isNull("scheduleMinutes")) null
                else json.optLong("scheduleMinutes")
        )
    }
}
