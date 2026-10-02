package com.monu.mobile.feature.conversation

import com.monu.mobile.feature.intelligence.MONUMasterBrain

data class MONUChatSubmission(
    val accepted: Boolean,
    val result: MONUConversationExecutionResult?,
    val state: MONUConversationUiState
)

class MONUChatCoordinator(
    private val masterBrain: MONUMasterBrain,
    private val adapter: MONUConversationUiAdapter =
        MONUConversationUiAdapter(masterBrain)
) {

    fun state(): MONUConversationUiState {
        return adapter.currentState()
    }

    suspend fun submit(
        input: String
    ): MONUChatSubmission {

        val cleanInput = input.trim()

        if (cleanInput.isBlank()) {
            return MONUChatSubmission(
                accepted = false,
                result = null,
                state = adapter.currentState()
            )
        }

        val result = adapter.submit(cleanInput)

        return MONUChatSubmission(
            accepted = true,
            result = result,
            state = adapter.currentState()
        )
    }

    fun clearConversation() {
        adapter.clear()
    }
}
