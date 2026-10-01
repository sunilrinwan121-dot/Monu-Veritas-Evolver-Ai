package com.monu.mobile.feature.rules

import com.monu.mobile.domain.model.MONURule
import com.monu.mobile.domain.model.MONURuleStatus

class MONURulesEngine {

    fun demoRules(): List<MONURule> {
        return listOf(
            MONURule(
                id = "connection_rule",
                name = "Connection Recovery Rule",
                description = "Designed to react to verified connection recovery events.",
                status = MONURuleStatus.UNKNOWN
            ),
            MONURule(
                id = "task_completion_rule",
                name = "Task Completion Rule",
                description = "Designed to trigger actions after verified task completion.",
                status = MONURuleStatus.UNKNOWN
            )
        )
    }

    fun evaluate(rule: MONURule): MONURuleStatus {
        return MONURuleStatus.UNKNOWN
    }
}
