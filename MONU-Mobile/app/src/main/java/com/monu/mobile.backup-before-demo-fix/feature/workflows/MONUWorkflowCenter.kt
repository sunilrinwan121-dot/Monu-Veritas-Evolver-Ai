package com.monu.mobile.feature.workflows

import com.monu.mobile.domain.model.MONUWorkflow
import com.monu.mobile.domain.model.MONUWorkflowRun
import com.monu.mobile.domain.model.MONUWorkflowStatus

class MONUWorkflowCenter {

    fun demoWorkflows(): List<MONUWorkflow> {
        return listOf(
            MONUWorkflow(
                id = "daily_review",
                name = "Daily System Review",
                description = "Architecture placeholder for automated daily review.",
                status = MONUWorkflowStatus.UNKNOWN
            ),
            MONUWorkflow(
                id = "project_pipeline",
                name = "Project Pipeline",
                description = "Architecture placeholder for project task automation.",
                status = MONUWorkflowStatus.UNKNOWN
            )
        )
    }

    fun createRun(workflow: MONUWorkflow): MONUWorkflowRun {
        return MONUWorkflowRun(
            id = "run_${workflow.id}",
            workflowId = workflow.id,
            status = MONUWorkflowStatus.UNKNOWN,
            message = "Real workflow execution requires a verified execution engine."
        )
    }
}
