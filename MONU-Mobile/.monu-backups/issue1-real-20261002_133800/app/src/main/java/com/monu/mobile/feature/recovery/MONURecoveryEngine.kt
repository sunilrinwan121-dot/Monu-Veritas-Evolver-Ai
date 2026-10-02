package com.monu.mobile.feature.recovery

import com.monu.mobile.domain.model.RecoveryCheckpoint
import com.monu.mobile.domain.model.RecoveryPlan
import com.monu.mobile.domain.model.RecoveryResult

class MONURecoveryEngine {

    fun checkpoints(): List<RecoveryCheckpoint> {
        return emptyList()
    }

    fun planRecovery(): List<RecoveryPlan> {
        return emptyList()
    }

    fun recover(plan: RecoveryPlan): RecoveryResult? {
        return null
    }
}
