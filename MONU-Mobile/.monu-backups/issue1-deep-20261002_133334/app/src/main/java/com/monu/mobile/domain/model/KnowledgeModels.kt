package com.monu.mobile.domain.model

enum class MONUKnowledgeSource {
    USER,
    PROJECT,
    DOCUMENT,
    CONVERSATION,
    SERVER,
    SYSTEM,
    UNKNOWN
}

enum class MONUKnowledgeStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED,
    UNKNOWN
}

data class MONUKnowledgeItem(
    val id: String,
    val title: String,
    val content: String,
    val source: MONUKnowledgeSource,
    val status: MONUKnowledgeStatus = MONUKnowledgeStatus.UNKNOWN,
    val tags: List<String> = emptyList(),
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)

data class MONUKnowledgeCollection(
    val id: String,
    val name: String,
    val description: String,
    val itemCount: Int = 0
)
