package com.monu.mobile.feature.planning

import com.monu.mobile.domain.model.MONUPlan
import com.monu.mobile.domain.model.MONUPlanRisk
import com.monu.mobile.domain.model.MONUPlanStatus
import com.monu.mobile.domain.model.MONUPlanStep
import com.monu.mobile.domain.model.MONUPlanStepStatus

class MONUPlanningIntelligence {

    fun createPlan(
        id: String,
        goal: String,
        steps: List<MONUPlanStep>,
        risks: List<MONUPlanRisk> = emptyList()
    ): MONUPlan {
        return MONUPlan(
            id = id,
            goal = goal,
            status = if (steps.isEmpty()) {
                MONUPlanStatus.UNKNOWN
            } else {
                MONUPlanStatus.CREATED
            },
            steps = steps,
            risks = risks,
            source = "LOCAL_ARCHITECTURE"
        )
    }

    fun nextExecutableStep(
        plan: MONUPlan
    ): MONUPlanStep? {
        return plan.steps.firstOrNull { step ->
            step.status == MONUPlanStepStatus.PENDING ||
            step.status == MONUPlanStepStatus.READY
        }
    }

    fun updateStepStatus(
        plan: MONUPlan,
        stepId: String,
        status: MONUPlanStepStatus
    ): MONUPlan {
        val updatedSteps = plan.steps.map { step ->
            if (step.id == stepId) {
                step.copy(status = status)
            } else {
                step
            }
        }

        return plan.copy(steps = updatedSteps)
    }
}
