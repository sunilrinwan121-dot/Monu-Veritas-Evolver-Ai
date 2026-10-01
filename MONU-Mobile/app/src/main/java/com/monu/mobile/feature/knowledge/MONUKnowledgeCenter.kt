package com.monu.mobile.feature.knowledge

import com.monu.mobile.domain.model.MONUKnowledgeItem
import com.monu.mobile.domain.model.MONUKnowledgeSource
import com.monu.mobile.domain.model.MONUKnowledgeStatus

class MONUKnowledgeCenter {

    fun demoKnowledge(): List<MONUKnowledgeItem> = emptyList()

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
