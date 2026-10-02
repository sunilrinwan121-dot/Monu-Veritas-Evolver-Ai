package com.monu.mobile.feature.intelligence

import com.monu.mobile.core.network.MONUNetworkMonitor
import com.monu.mobile.feature.context.MONUContextIntelligence
import com.monu.mobile.domain.model.InternetKnowledgeState
import com.monu.mobile.feature.gemini.MONUGeminiIntelligenceEngine
import com.monu.mobile.feature.knowledge.MONUInternetKnowledgeEngine
import com.monu.mobile.feature.offline.MONUOfflineCommandRequest
import com.monu.mobile.feature.offline.MONUOfflineCommandRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class MONUMasterBrainId {
    CONTEXT_INTELLIGENCE,
    OFFLINE_COMMAND,
    GEMINI_INTELLIGENCE,
    INTERNET_KNOWLEDGE
}

data class MONUMasterBrainCapability(
    val id: MONUMasterBrainId,
    val name: String,
    val purpose: String,
    val capabilities: List<String>,
    val priority: Int
)

data class MONUMasterBrainDecision(
    val selectedBrain: MONUMasterBrainId?,
    val reason: String,
    val online: Boolean
)

data class MONUMasterBrainResult(
    val brain: MONUMasterBrainId?,
    val text: String,
    val success: Boolean,
    val decision: MONUMasterBrainDecision
)

class MONUMasterBrain(
    private val networkMonitor: MONUNetworkMonitor,
    private val contextBrain: MONUContextIntelligence =
        MONUContextIntelligence(),
    private val geminiBrain: MONUGeminiIntelligenceEngine =
        MONUGeminiIntelligenceEngine(),
    private val intelligenceHub: MONUIntelligenceHub =
        MONUIntelligenceHub(geminiBrain),
    private val offlineBrain: MONUOfflineCommandRouter =
        MONUOfflineCommandRouter(),
    private val internetBrain: MONUInternetKnowledgeEngine =
        MONUInternetKnowledgeEngine()
) {

    fun capabilities(): List<MONUMasterBrainCapability> {
        return listOf(
            MONUMasterBrainCapability(
                id = MONUMasterBrainId.CONTEXT_INTELLIGENCE,
                name = contextBrain.masterName(),
                purpose = contextBrain.masterPurpose(),
                capabilities = contextBrain.masterCapabilities(),
                priority = 0
            ),
            MONUMasterBrainCapability(
                id = MONUMasterBrainId.OFFLINE_COMMAND,
                name = "Offline Command Intelligence",
                purpose = "Understands and executes supported local commands.",
                capabilities = offlineBrain.masterCapabilities(),
                priority = 1
            ),
            MONUMasterBrainCapability(
                id = MONUMasterBrainId.GEMINI_INTELLIGENCE,
                name = "Gemini Intelligence",
                purpose =
                    "Performs cloud AI reasoning with multi-model fallback.",
                capabilities = geminiBrain.masterCapabilities(),
                priority = 2
            ),
            MONUMasterBrainCapability(
                id = MONUMasterBrainId.INTERNET_KNOWLEDGE,
                name = "Internet Knowledge Intelligence",
                purpose = internetBrain.masterPurpose(),
                capabilities = internetBrain.masterCapabilities(),
                priority = 3
            )
        )
    }

    fun intelligenceHealth(): String {
        return intelligenceHub.healthReport()
    }

    suspend fun answer(query: String): MONUMasterBrainResult =
        withContext(Dispatchers.IO) {

            val cleanQuery = query.trim()
            val isOnline = networkMonitor.isOnline()

            if (cleanQuery.isBlank()) {
                return@withContext MONUMasterBrainResult(
                    brain = null,
                    text = "Please enter a valid request.",
                    success = false,
                    decision = MONUMasterBrainDecision(
                        selectedBrain = null,
                        reason = "Blank query",
                        online = isOnline
                    )
                )
            }

            val lower = cleanQuery.lowercase()

            if (
                lower == "monu health" ||
                lower == "monu intelligence health" ||
                lower == "intelligence status" ||
                lower == "capabilities"
            ) {
                return@withContext MONUMasterBrainResult(
                    brain = null,
                    text = intelligenceHealth(),
                    success = true,
                    decision = MONUMasterBrainDecision(
                        selectedBrain = null,
                        reason = "Master Brain health request",
                        online = isOnline
                    )
                )
            }

            if (contextBrain.canHandle(cleanQuery)) {
                return@withContext MONUMasterBrainResult(
                    brain = MONUMasterBrainId.CONTEXT_INTELLIGENCE,
                    text = contextBrain.answer(cleanQuery),
                    success = true,
                    decision = MONUMasterBrainDecision(
                        selectedBrain = MONUMasterBrainId.CONTEXT_INTELLIGENCE,
                        reason = "Context Intelligence can handle this request",
                        online = isOnline
                    )
                )
            }

            if (offlineBrain.canHandle(cleanQuery)) {
                val response = offlineBrain.execute(
                    MONUOfflineCommandRequest(cleanQuery)
                )

                return@withContext MONUMasterBrainResult(
                    brain = MONUMasterBrainId.OFFLINE_COMMAND,
                    text = response.response,
                    success = response.handled,
                    decision = MONUMasterBrainDecision(
                        selectedBrain = MONUMasterBrainId.OFFLINE_COMMAND,
                        reason = "Offline Command Intelligence can handle this request",
                        online = isOnline
                    )
                )
            }

            if (isOnline && geminiBrain.isConfigured()) {
                val result = geminiBrain.ask(cleanQuery)

                if (result.success && result.text.isNotBlank()) {
                    return@withContext MONUMasterBrainResult(
                        brain = MONUMasterBrainId.GEMINI_INTELLIGENCE,
                        text = result.text,
                        success = true,
                        decision = MONUMasterBrainDecision(
                            selectedBrain = MONUMasterBrainId.GEMINI_INTELLIGENCE,
                            reason = "Gemini is configured and returned a successful answer",
                            online = isOnline
                        )
                    )
                }
            }

            if (!isOnline) {
                return@withContext MONUMasterBrainResult(
                    brain = null,
                    text = "Internet is unavailable. I can still handle supported offline commands.",
                    success = false,
                    decision = MONUMasterBrainDecision(
                        selectedBrain = null,
                        reason = "No online intelligence available and no offline capability matched",
                        online = false
                    )
                )
            }

            val internetResult =
                internetBrain.search(cleanQuery)

            if (
                internetResult.state == InternetKnowledgeState.SUCCESS &&
                internetResult.summary.isNotBlank()
            ) {
                val text = buildString {
                    if (internetResult.title.isNotBlank()) {
                        append(internetResult.title)
                        append("\n\n")
                    }

                    append(internetResult.summary)

                    if (internetResult.source.isNotBlank()) {
                        append("\n\nSource: ")
                        append(internetResult.source)
                    }
                }

                return@withContext MONUMasterBrainResult(
                    brain = MONUMasterBrainId.INTERNET_KNOWLEDGE,
                    text = text,
                    success = true,
                    decision = MONUMasterBrainDecision(
                        selectedBrain = MONUMasterBrainId.INTERNET_KNOWLEDGE,
                        reason = "Higher-priority intelligence did not return an answer; internet knowledge succeeded",
                        online = isOnline
                    )
                )
            }

            MONUMasterBrainResult(
                brain = null,
                text =
                    "No available intelligence capability could complete this request.",
                success = false,
                decision = MONUMasterBrainDecision(
                    selectedBrain = null,
                    reason = "All eligible intelligence capabilities were exhausted",
                    online = isOnline
                )
            )
        }
}
