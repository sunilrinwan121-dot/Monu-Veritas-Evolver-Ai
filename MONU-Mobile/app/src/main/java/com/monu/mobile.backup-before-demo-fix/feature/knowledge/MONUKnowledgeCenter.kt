package com.monu.mobile.feature.knowledge

import com.monu.mobile.domain.model.MONUKnowledgeItem
import com.monu.mobile.domain.model.MONUKnowledgeSource
import com.monu.mobile.domain.model.MONUKnowledgeStatus

class MONUKnowledgeCenter {

    fun demoKnowledge(): List<MONUKnowledgeItem> {
        return listOf(
            MONUKnowledgeItem(
                id = "architecture",
                title = "MONU Mobile Architecture",
                content = "Project architecture knowledge can be indexed here.",
                source = MONUKnowledgeSource.PROJECT,
                status = MONUKnowledgeStatus.UNKNOWN,
                tags = listOf("architecture", "mobile")
            ),
            MONUKnowledgeItem(
                id = "owner_preferences",
                title = "Owner Knowledge",
                content = "Explicitly saved and verified owner preferences may appear here.",
                source = MONUKnowledgeSource.USER,
                status = MONUKnowledgeStatus.UNKNOWN,
                tags = listOf("owner", "preferences")
            )
        )
    }

    fun search(
        query: String,
        items: List<MONUKnowledgeItem>
    ): List<MONUKnowledgeItem> {
        if (query.isBlank()) return items

        return items.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.content.contains(query, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }
}
