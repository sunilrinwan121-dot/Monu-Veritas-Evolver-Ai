package com.monu.mobile.feature.conversation

import com.monu.mobile.feature.intelligence.MONUMasterBrain

class MONUChatScreenIntegration(
    private val masterBrain: MONUMasterBrain,
    private val coordinator: MONUChatCoordinator =
        MONUChatCoordinator(masterBrain)
) {

    fun messages(): List<MONUConversationMessage> {
        return coordinator.state().messages
    }

    fun isProcessing(): Boolean {
        return coordinator.state().isProcessing
    }

    suspend fun submit(
        command: String
    ): MONUChatSubmission {
        return coordinator.submit(command)
    }

    fun clear() {
        coordinator.clearConversation()
    }
}
