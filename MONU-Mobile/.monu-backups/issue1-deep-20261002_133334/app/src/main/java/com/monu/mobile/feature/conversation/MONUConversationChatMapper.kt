package com.monu.mobile.feature.conversation

import com.monu.mobile.domain.model.ChatMessage
import com.monu.mobile.domain.model.MessageRole

object MONUConversationChatMapper {

    fun toChatMessages(
        messages: List<MONUConversationMessage>,
        conversationId: String = "default"
    ): List<ChatMessage> {
        return messages.map { message ->
            ChatMessage(
                id = message.id.toString(),
                conversationId = conversationId,
                content = message.content,
                role = message.role.toChatRole(),
                timestamp = message.timestamp
            )
        }
    }

    private fun MONUConversationRole.toChatRole(): MessageRole {
        return when (this) {
            MONUConversationRole.USER -> MessageRole.OWNER
            MONUConversationRole.ASSISTANT -> MessageRole.MONU
            MONUConversationRole.SYSTEM -> MessageRole.SYSTEM
        }
    }
}
