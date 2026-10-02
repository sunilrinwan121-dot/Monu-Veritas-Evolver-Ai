package com.monu.mobile.feature.conversation

data class MONUConversationState(
    val messages: List<MONUConversationMessage> = emptyList(),
    val isProcessing: Boolean = false,
    val lastError: String? = null,
    val restored: Boolean = false
)

class MONUConversationStateController(
    private val repository: MONUConversationRepository =
        MONULocalConversationRepository(),
    private val persistence: MONUConversationPersistence =
        MONULocalConversationSnapshotStore()
) {

    private var currentState =
        MONUConversationState()

    fun state(): MONUConversationState {
        return currentState
    }

    fun beginProcessing() {
        currentState =
            currentState.copy(
                isProcessing = true,
                lastError = null
            )
    }

    fun finishProcessing() {
        currentState =
            currentState.copy(
                isProcessing = false
            )

        persist()
    }

    fun addUserMessage(
        content: String
    ): MONUConversationMessage {

        val message =
            repository.append(
                role = MONUConversationRole.USER,
                content = content
            )

        refreshMessages()
        persist()

        return message
    }

    fun addAssistantMessage(
        content: String
    ): MONUConversationMessage {

        val message =
            repository.append(
                role = MONUConversationRole.ASSISTANT,
                content = content
            )

        refreshMessages()
        persist()

        return message
    }

    fun setError(
        message: String
    ) {
        currentState =
            currentState.copy(
                isProcessing = false,
                lastError = message
            )

        persist()
    }

    fun restoreConversation() {
        val snapshot = persistence.restore()

        currentState =
            if (snapshot == null) {
                currentState.copy(
                    restored = true
                )
            } else {
                MONUConversationState(
                    messages = snapshot.messages,
                    isProcessing = false,
                    lastError = null,
                    restored = true
                )
            }
    }

    fun clearConversation() {
        repository.clear()
        persistence.clear()

        currentState =
            MONUConversationState(
                restored = true
            )
    }

    fun snapshot(): MONUConversationSnapshot {
        return MONUConversationSnapshot(
            messages = currentState.messages,
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun refreshMessages() {
        currentState =
            currentState.copy(
                messages = repository.messages()
            )
    }

    private fun persist() {
        persistence.save(snapshot())
    }
}
