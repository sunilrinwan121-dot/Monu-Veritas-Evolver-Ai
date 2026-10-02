package com.monu.mobile.domain.model

enum class InternetKnowledgeState {
    SUCCESS,
    NOT_FOUND,
    NETWORK_ERROR,
    INVALID_QUERY
}

data class InternetKnowledgeResult(
    val query: String,
    val title: String,
    val summary: String,
    val source: String,
    val state: InternetKnowledgeState,
    val errorMessage: String? = null
)
