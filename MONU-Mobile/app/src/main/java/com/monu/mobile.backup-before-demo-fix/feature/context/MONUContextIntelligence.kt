package com.monu.mobile.feature.context

import com.monu.mobile.domain.model.MONUContextItem
import com.monu.mobile.domain.model.MONUContextSnapshot
import com.monu.mobile.domain.model.MONUContextStatus
import com.monu.mobile.domain.model.MONUContextType

class MONUContextIntelligence {

    fun masterName(): String =
        "Context Intelligence"

    fun masterPurpose(): String =
        "Stores, prioritizes and snapshots contextual information."

    fun masterCapabilities(): List<String> =
        listOf(
            "Context assembly",
            "Context prioritization",
            "Context snapshot creation",
            "Project context support",
            "Command context support",
            "Conversation context support",
            "Task context support",
            "Document context support",
            "Session context support",
            "System context support"
        )

    fun masterHealthCheck(): Boolean =
        runCatching {
            demoContext()
        }.isSuccess

    fun canHandle(query: String): Boolean {
        return when (query.trim().lowercase()) {
            "context",
            "context status",
            "show context",
            "context snapshot",
            "prioritize context" -> true
            else -> false
        }
    }

    fun answer(query: String): String {
        return when (query.trim().lowercase()) {
            "context",
            "context status",
            "show context" -> {
                prioritize(demoContext()).joinToString(
                    separator = "\n"
                ) { item ->
                    "${item.priority}. ${item.title}: ${item.summary}"
                }
            }

            "context snapshot" -> {
                val snapshot = createSnapshot(
                    title = "MONU Context Snapshot",
                    items = prioritize(demoContext())
                )

                "Snapshot: ${snapshot.title}\nItems: ${snapshot.items.size}"
            }

            "prioritize context" -> {
                prioritize(demoContext()).joinToString(
                    separator = "\n"
                ) { item ->
                    "${item.priority}. ${item.title}"
                }
            }

            else -> "Context Intelligence could not handle this request."
        }
    }

    fun demoContext(): List<MONUContextItem> {
        return listOf(
            MONUContextItem(
                id = "project_context",
                type = MONUContextType.PROJECT,
                title = "Current Project Context",
                summary = "Verified project information can be assembled here.",
                status = MONUContextStatus.UNKNOWN,
                priority = 10
            ),
            MONUContextItem(
                id = "command_context",
                type = MONUContextType.COMMAND,
                title = "Recent Command Context",
                summary = "Real command history may later provide contextual continuity.",
                status = MONUContextStatus.UNKNOWN,
                priority = 8
            )
        )
    }

    fun createSnapshot(
        title: String,
        items: List<MONUContextItem>
    ): MONUContextSnapshot {
        return MONUContextSnapshot(
            id = "context_snapshot",
            title = title,
            items = items
        )
    }

    fun prioritize(
        items: List<MONUContextItem>
    ): List<MONUContextItem> {
        return items.sortedByDescending { it.priority }
    }
}
