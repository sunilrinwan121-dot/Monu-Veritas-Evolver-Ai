package com.monu.mobile.feature.conversation

import com.monu.mobile.feature.intelligence.MONUMasterBrain
import com.monu.mobile.feature.intelligence.MONUMasterBrainResult
import com.monu.mobile.feature.intelligence.MONUMasterBrainDecision
import com.monu.mobile.feature.offline.MONUOfflineCommandResponse

data class MONUConversationExecutionResult(
    val userMessage: MONUConversationMessage,
    val assistantMessage: MONUConversationMessage,
    val masterBrainResult: MONUMasterBrainResult,
    val commandResponse: MONUOfflineCommandResponse? = null
)

class MONUOfflineConversationBridge(
    private val masterBrain: MONUMasterBrain,
    private val stateController: MONUConversationStateController =
        MONUConversationStateController()
) {

    fun conversationState(): MONUConversationState {
        return stateController.state()
    }

    suspend fun execute(
        command: String
    ): MONUConversationExecutionResult {

        stateController.beginProcessing()

        return try {
            val userMessage =
                stateController.addUserMessage(
                    content = command
                )

            val brainResult =
                masterBrain.answer(command)

            val assistantMessage =
                stateController.addAssistantMessage(
                    content = brainResult.text
                )

            stateController.finishProcessing()

            MONUConversationExecutionResult(
                userMessage = userMessage,
                assistantMessage = assistantMessage,
                masterBrainResult = brainResult
            )
        } catch (error: Exception) {

            val safeMessage =
                error.message
                    ?: "MONU could not process this request."

            stateController.setError(safeMessage)

            val userMessage =
                stateController.addUserMessage(
                    content = command
                )

            val assistantMessage =
                stateController.addAssistantMessage(
                    content = safeMessage
                )

            MONUConversationExecutionResult(
                userMessage = userMessage,
                assistantMessage = assistantMessage,
                masterBrainResult =
                    MONUMasterBrainResult(
                        brain = null,
                        text = safeMessage,
                        success = false,
                        decision = MONUMasterBrainDecision(
                            selectedBrain = null,
                            reason = "Conversation execution failed before a Master Brain result could be completed",
                            online = false
                        )
                    )
            )
        }
    }

    fun clearConversation() {
        stateController.clearConversation()
    }
}
