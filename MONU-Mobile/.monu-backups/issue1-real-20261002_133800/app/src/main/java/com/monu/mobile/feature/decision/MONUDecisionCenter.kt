package com.monu.mobile.feature.decision

import com.monu.mobile.domain.model.MONUDecision
import com.monu.mobile.domain.model.MONUDecisionOption
import com.monu.mobile.domain.model.MONUDecisionStatus

class MONUDecisionCenter {

    fun createDecision(
        id: String,
        title: String,
        description: String,
        options: List<MONUDecisionOption>
    ): MONUDecision {
        return MONUDecision(
            id = id,
            title = title,
            description = description,
            status = if (options.isEmpty()) {
                MONUDecisionStatus.UNKNOWN
            } else {
                MONUDecisionStatus.PENDING
            },
            options = options,
            source = "VERIFIED_SOURCE_REQUIRED"
        )
    }

    fun selectOption(
        decision: MONUDecision,
        optionId: String
    ): MONUDecision {
        val exists = decision.options.any { it.id == optionId }

        if (!exists) return decision

        return decision.copy(
            status = MONUDecisionStatus.SELECTED,
            selectedOptionId = optionId
        )
    }
}
