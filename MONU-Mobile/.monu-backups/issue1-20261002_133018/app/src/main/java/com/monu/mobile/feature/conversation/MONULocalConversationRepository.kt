package com.monu.mobile.feature.conversation

class MONULocalConversationRepository :
    MONUConversationRepository {

    private val conversationMessages =
        mutableListOf<MONUConversationMessage>()

    private var nextId = 1L

    override fun messages(): List<MONUConversationMessage> {
        return conversationMessages.toList()
    }

    override fun append(
        role: MONUConversationRole,
        content: String
    ): MONUConversationMessage {

        val message =
            MONUConversationMessage(
                id = nextId++,
                role = role,
                content = content,
                timestamp = System.currentTimeMillis()
            )

        conversationMessages += message

        return message
    }

    override fun clear() {
        conversationMessages.clear()
        nextId = 1L
    }

    override fun snapshot(): MONUConversationSnapshot {
        return MONUConversationSnapshot(
            messages = messages(),
            updatedAt = System.currentTimeMillis()
        )
    }
}
