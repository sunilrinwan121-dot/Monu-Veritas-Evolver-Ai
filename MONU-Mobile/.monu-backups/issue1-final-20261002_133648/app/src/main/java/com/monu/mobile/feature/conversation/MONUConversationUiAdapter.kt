package com.monu.mobile.feature.conversation

import com.monu.mobile.feature.intelligence.MONUMasterBrain

data class MONUConversationUiState(
    val messages: List<MONUConversationMessage> = emptyList(),
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

class MONUConversationUiAdapter(
    private val masterBrain: MONUMasterBrain,
    private val pipeline: MONUConversationPipeline =
        MONUConversationPipeline(masterBrain)
) {

    fun currentState(): MONUConversationUiState {
        val state = pipeline.state()

        return MONUConversationUiState(
            messages = state.messages,
            isProcessing = state.isProcessing,
            errorMessage = state.lastError
        )
    }

    suspend fun submit(
        input: String
    ): MONUConversationExecutionResult {
        return pipeline.submit(input)
    }

    fun clear() {
        pipeline.clear()
    }
}
