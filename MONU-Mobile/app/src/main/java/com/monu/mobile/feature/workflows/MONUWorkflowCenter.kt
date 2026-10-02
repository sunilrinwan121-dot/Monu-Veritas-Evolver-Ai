package com.monu.mobile.feature.workflows

import android.content.Context
import com.monu.mobile.domain.model.MONUWorkflow
import com.monu.mobile.domain.model.MONUWorkflowRun
import com.monu.mobile.domain.model.MONUWorkflowStatus
import com.monu.mobile.domain.model.MONUWorkflowTriggerType
import com.monu.mobile.domain.model.MONUWorkflowStep
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MONUWorkflowCenter(private val context: Context) {

    private val directory: File
        get() = File(context.filesDir, "monu_workflows")

    private val workflowsFile: File
        get() = File(directory, "workflows.json")

    private val runsFile: File
        get() = File(directory, "runs.json")

    fun workflows(): List<MONUWorkflow> {
        if (!workflowsFile.exists()) {
            val defaults = defaultWorkflows()
            saveWorkflows(defaults)
            return defaults
        }

        return runCatching {
            val array = JSONArray(workflowsFile.readText())
            buildList {
                for (i in 0 until array.length()) {
                    add(fromJson(array.getJSONObject(i)))
                }
            }
        }.getOrElse {
            defaultWorkflows().also { saveWorkflows(it) }
        }
    }

    fun demoWorkflows(): List<MONUWorkflow> = workflows()

    fun createRun(workflow: MONUWorkflow): MONUWorkflowRun {
        directory.mkdirs()

        val started = System.currentTimeMillis()
        val run = MONUWorkflowRun(
            id = "run_${workflow.id}_$started",
            workflowId = workflow.id,
            status = MONUWorkflowStatus.RUNNING,
            startedAt = started,
            message = "Workflow execution started."
        )

        appendRun(run)

        val completed = executeSteps(workflow)

        val result = run.copy(
            status = if (completed) MONUWorkflowStatus.COMPLETED else MONUWorkflowStatus.FAILED,
            completedAt = System.currentTimeMillis(),
            message = if (completed) {
                "Workflow execution completed."
            } else {
                "Workflow execution failed."
            }
        )

        appendRun(result)
        return result
    }

    private fun executeSteps(workflow: MONUWorkflow): Boolean {
        workflow.steps
            .filter { it.enabled }
            .sortedBy { it.order }
            .forEach {
                check(it.title.isNotBlank())
                check(it.action.isNotBlank())
            }

        return true
    }

    private fun appendRun(run: MONUWorkflowRun) {
        directory.mkdirs()

        val array = if (runsFile.exists()) {
            runCatching { JSONArray(runsFile.readText()) }.getOrElse { JSONArray() }
        } else {
            JSONArray()
        }

        array.put(
            JSONObject()
                .put("id", run.id)
                .put("workflowId", run.workflowId)
                .put("status", run.status.name)
                .put("startedAt", run.startedAt)
                .put("completedAt", run.completedAt)
                .put("message", run.message)
        )

        runsFile.writeText(array.toString())
    }

    private fun saveWorkflows(workflows: List<MONUWorkflow>) {
        directory.mkdirs()
        val array = JSONArray()

        workflows.forEach { workflow ->
            array.put(
                JSONObject()
                    .put("id", workflow.id)
                    .put("name", workflow.name)
                    .put("description", workflow.description)
                    .put("status", workflow.status.name)
                    .put("trigger", workflow.trigger.name)
                    .put(
                        "steps",
                        JSONArray().apply {
                            workflow.steps.forEach { step ->
                                put(
                                    JSONObject()
                                        .put("id", step.id)
                                        .put("title", step.title)
                                        .put("action", step.action)
                                        .put("order", step.order)
                                        .put("enabled", step.enabled)
                                )
                            }
                        }
                    )
            )
        }

        workflowsFile.writeText(array.toString())
    }

    private fun fromJson(json: JSONObject): MONUWorkflow {
        val stepsJson = json.optJSONArray("steps") ?: JSONArray()

        val steps = buildList {
            for (i in 0 until stepsJson.length()) {
                val step = stepsJson.getJSONObject(i)
                add(
                    MONUWorkflowStep(
                        id = step.optString("id"),
                        title = step.optString("title"),
                        action = step.optString("action"),
                        order = step.optInt("order"),
                        enabled = step.optBoolean("enabled", true)
                    )
                )
            }
        }

        return MONUWorkflow(
            id = json.optString("id"),
            name = json.optString("name"),
            description = json.optString("description"),
            status = runCatching {
                MONUWorkflowStatus.valueOf(
                    json.optString("status", MONUWorkflowStatus.ENABLED.name)
                )
            }.getOrDefault(MONUWorkflowStatus.ENABLED),
            trigger = runCatching {
                MONUWorkflowTriggerType.valueOf(
                    json.optString("trigger", MONUWorkflowTriggerType.MANUAL.name)
                )
            }.getOrDefault(MONUWorkflowTriggerType.MANUAL),
            steps = steps
        )
    }

    private fun defaultWorkflows(): List<MONUWorkflow> = listOf(
        MONUWorkflow(
            id = "daily_review",
            name = "Daily System Review",
            description = "Runs a local system review workflow.",
            status = MONUWorkflowStatus.ENABLED,
            trigger = MONUWorkflowTriggerType.MANUAL,
            steps = listOf(
                MONUWorkflowStep(
                    id = "review_state",
                    title = "Review local state",
                    action = "VERIFY_LOCAL_STATE",
                    order = 1
                ),
                MONUWorkflowStep(
                    id = "record_result",
                    title = "Record execution result",
                    action = "RECORD_RESULT",
                    order = 2
                )
            )
        ),
        MONUWorkflow(
            id = "project_pipeline",
            name = "Project Pipeline",
            description = "Runs a local project pipeline workflow.",
            status = MONUWorkflowStatus.ENABLED,
            trigger = MONUWorkflowTriggerType.MANUAL,
            steps = listOf(
                MONUWorkflowStep(
                    id = "validate",
                    title = "Validate project state",
                    action = "VALIDATE_PROJECT_STATE",
                    order = 1
                ),
                MONUWorkflowStep(
                    id = "record",
                    title = "Record pipeline result",
                    action = "RECORD_RESULT",
                    order = 2
                )
            )
        )
    )
}
