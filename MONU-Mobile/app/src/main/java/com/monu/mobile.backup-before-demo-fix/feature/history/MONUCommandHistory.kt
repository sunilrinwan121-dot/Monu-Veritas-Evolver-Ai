package com.monu.mobile.feature.history

import com.monu.mobile.domain.model.MONUCommandHistoryEntry
import com.monu.mobile.domain.model.MONUCommandPattern

class MONUCommandHistory {

    private val commands = mutableListOf<MONUCommandHistoryEntry>()

    fun add(entry: MONUCommandHistoryEntry) {
        commands += entry
    }

    fun all(): List<MONUCommandHistoryEntry> {
        return commands.sortedByDescending { it.timestamp }
    }

    fun analyzePatterns(): List<MONUCommandPattern> {
        if (commands.isEmpty()) return emptyList()

        val grouped = commands
            .groupBy { it.command.trim().lowercase() }

        return grouped.map { (command, entries) ->
            MONUCommandPattern(
                pattern = command,
                count = entries.size,
                description = "This command pattern occurred ${entries.size} time(s)."
            )
        }.sortedByDescending { it.count }
    }

    fun clear() {
        commands.clear()
    }
}
