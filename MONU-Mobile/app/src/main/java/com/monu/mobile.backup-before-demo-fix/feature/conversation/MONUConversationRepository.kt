package com.monu.mobile.feature.conversation

interface MONUConversationRepository {

    fun messages(): List<MONUConversationMessage>

    fun append(
        role: MONUConversationRole,
        content: String
    ): MONUConversationMessage

    fun clear()

    fun snapshot(): MONUConversationSnapshot
}
