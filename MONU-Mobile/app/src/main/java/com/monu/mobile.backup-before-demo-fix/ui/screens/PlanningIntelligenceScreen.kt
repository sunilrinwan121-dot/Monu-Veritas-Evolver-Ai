package com.monu.mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monu.mobile.domain.model.MONUPlan
import com.monu.mobile.domain.model.MONUPlanStatus
import com.monu.mobile.domain.model.MONUPlanStep
import com.monu.mobile.domain.model.MONUPlanStepStatus

@Composable
fun PlanningIntelligenceScreen() {

    val plan = MONUPlan(
        id = "architecture_plan",
        goal = "Future MONU planning pipeline",
        status = MONUPlanStatus.CREATED,
        source = "LOCAL_ARCHITECTURE",
        steps = listOf(
            MONUPlanStep(
                id = "understand",
                title = "Understand Goal",
                description = "Analyze the real command or objective.",
                status = MONUPlanStepStatus.PENDING
            ),
            MONUPlanStep(
                id = "plan",
                title = "Generate Plan",
                description = "Create ordered execution steps.",
                status = MONUPlanStepStatus.PENDING
            ),
            MONUPlanStep(
                id = "verify",
                title = "Verify Result",
                description = "Confirm execution using real evidence.",
                status = MONUPlanStepStatus.PENDING
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("MONU Planning Intelligence")
        Text("Goal: ${plan.goal}")
        Text("Plan Status: ${plan.status}")

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(plan.steps) { step ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(step.title)
                        Text(step.description)
                        Text("Status: ${step.status}")
                        Text("Dependencies: ${step.dependencies.size}")
                    }
                }
            }
        }
    }
}
