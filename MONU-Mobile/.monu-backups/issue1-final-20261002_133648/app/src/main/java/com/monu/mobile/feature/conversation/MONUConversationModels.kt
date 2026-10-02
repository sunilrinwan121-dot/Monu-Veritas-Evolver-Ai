package com.monu.mobile.feature.conversation

enum class MONUConversationRole {
    USER,
    ASSISTANT,
    SYSTEM
}

data class MONUConversationMessage(
    val id: Long,
    val role: MONUConversationRole,
    val content: String,
    val timestamp: Long
)

data class MONUConversationSnapshot(
    val messages: List<MONUConversationMessage>,
    val updatedAt: Long
)
