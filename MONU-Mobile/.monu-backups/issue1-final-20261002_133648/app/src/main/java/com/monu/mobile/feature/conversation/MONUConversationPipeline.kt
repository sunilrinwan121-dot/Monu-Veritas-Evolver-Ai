package com.monu.mobile.feature.conversation

import com.monu.mobile.feature.intelligence.MONUMasterBrain

class MONUConversationPipeline(
    private val masterBrain: MONUMasterBrain,
    private val bridge: MONUOfflineConversationBridge =
        MONUOfflineConversationBridge(masterBrain)
) {

    fun state(): MONUConversationState {
        return bridge.conversationState()
    }

    suspend fun submit(
        input: String
    ): MONUConversationExecutionResult {

        val cleanInput = input.trim()

        return bridge.execute(cleanInput)
    }

    fun clear() {
        bridge.clearConversation()
    }
}
