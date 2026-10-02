package com.monu.mobile.domain.model

data class ChatConversation(
    val id: String,
    val title: String,
    val messages: List<ChatMessage> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class MessageRole {
    OWNER,
    MONU,
    SYSTEM
}

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val content: String,
    val role: MessageRole,
    val timestamp: Long = System.currentTimeMillis(),
    val audioProgressMs: Long = 0L,
    val isAudioAvailable: Boolean = false
)
