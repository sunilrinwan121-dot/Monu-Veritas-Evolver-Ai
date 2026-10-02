package com.monu.mobile.feature.workflows

import com.monu.mobile.domain.model.MONUWorkflow
import com.monu.mobile.domain.model.MONUWorkflowRun
import com.monu.mobile.domain.model.MONUWorkflowStatus

class MONUWorkflowCenter {

    fun realWorkflows(): List<MONUWorkflow> = emptyList()

    fun createRun(workflow: MONUWorkflow): MONUWorkflowRun {
        return MONUWorkflowRun(
            id = "run_${workflow.id}",
            workflowId = workflow.id,
            status = MONUWorkflowStatus.UNKNOWN,
            message = "Workflow execution is unavailable because no verified execution engine is connected."
        )
    }
}
