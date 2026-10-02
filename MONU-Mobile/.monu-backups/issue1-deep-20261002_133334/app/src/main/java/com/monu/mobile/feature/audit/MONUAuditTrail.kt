package com.monu.mobile.feature.audit

import com.monu.mobile.domain.model.MONUAuditAction
import com.monu.mobile.domain.model.MONUAuditActor
import com.monu.mobile.domain.model.MONUAuditEntry
import com.monu.mobile.domain.model.MONUAuditResult

class MONUAuditTrail {

    private val entries = mutableListOf<MONUAuditEntry>()

    fun record(
        actor: MONUAuditActor,
        action: MONUAuditAction,
        result: MONUAuditResult,
        title: String,
        description: String,
        metadata: Map<String, String> = emptyMap()
    ) {
        entries += MONUAuditEntry(
            id = "audit-${System.currentTimeMillis()}-${entries.size}",
            timestamp = System.currentTimeMillis(),
            actor = actor,
            action = action,
            result = result,
            title = title,
            description = description,
            metadata = metadata
        )
    }

    fun all(): List<MONUAuditEntry> {
        return entries.sortedByDescending { it.timestamp }
    }

    fun clear() {
        entries.clear()
    }
}
