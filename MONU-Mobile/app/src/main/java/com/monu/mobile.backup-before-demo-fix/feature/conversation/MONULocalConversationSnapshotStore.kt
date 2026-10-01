package com.monu.mobile.feature.conversation

class MONULocalConversationSnapshotStore :
    MONUConversationPersistence {

    private var storedSnapshot:
        MONUConversationSnapshot? = null

    override fun save(
        snapshot: MONUConversationSnapshot
    ) {
        storedSnapshot = snapshot.copy(
            messages = snapshot.messages.toList()
        )
    }

    override fun restore(): MONUConversationSnapshot? {
        return storedSnapshot?.copy(
            messages = storedSnapshot
                ?.messages
                ?.toList()
                ?: emptyList()
        )
    }

    override fun clear() {
        storedSnapshot = null
    }
}
