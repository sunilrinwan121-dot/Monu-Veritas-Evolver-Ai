package com.monu.mobile.feature.rules

import com.monu.mobile.domain.model.MONURule
import com.monu.mobile.domain.model.MONURuleStatus

class MONURulesEngine {

    fun realRules(): List<MONURule> = emptyList()

    fun evaluate(rule: MONURule): MONURuleStatus {
        return MONURuleStatus.UNKNOWN
    }
}
