package com.monu.mobile.feature.conversation

interface MONUConversationPersistence {

    fun save(
        snapshot: MONUConversationSnapshot
    )

    fun restore(): MONUConversationSnapshot?

    fun clear()
}
