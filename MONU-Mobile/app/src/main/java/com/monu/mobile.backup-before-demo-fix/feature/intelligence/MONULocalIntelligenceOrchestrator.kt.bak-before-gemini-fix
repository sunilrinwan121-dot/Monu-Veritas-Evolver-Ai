package com.monu.mobile.feature.intelligence

import com.monu.mobile.feature.gemini.MONUGeminiIntelligenceEngine
import com.monu.mobile.feature.knowledge.MONUInternetKnowledgeEngine
import com.monu.mobile.feature.offline.MONUOfflineCommandRouter

/**
 * Local-first intelligence fallback.
 *
 * Order:
 * 1. Gemini intelligence when configured.
 * 2. Internet knowledge when Gemini is unavailable or cannot answer.
 * 3. Offline command engine when network intelligence is unavailable.
 *
 * This class intentionally keeps all fallback decisions inside the APK.
 */
class MONULocalIntelligenceOrchestrator {

    private val gemini = MONUGeminiIntelligenceEngine()
    private val internet = MONUInternetKnowledgeEngine()
    private val offline = MONUOfflineCommandRouter()

    fun answer(query: String): String {
        val clean = query.trim()

        if (clean.isBlank()) {
            return "Please enter a valid command or question."
        }

        // Gemini is the primary local AI intelligence layer.
        try {
            if (gemini.isConfigured()) {
                val result = gemini.generate(clean)

                if (result.isNotBlank()) {
                    return result
                }
            }
        } catch (_: Exception) {
            // Continue to the next local intelligence layer.
        }

        // Internet knowledge layer.
        try {
            val result = internet.search(clean)

            if (result.summary.isNotBlank()) {
                return buildString {
                    if (result.title.isNotBlank()) {
                        append(result.title)
                        append("\n\n")
                    }

                    append(result.summary)

                    if (result.source.isNotBlank()) {
                        append("\n\nSource: ")
                        append(result.source)
                    }
                }
            }
        } catch (_: Exception) {
            // Continue to offline intelligence.
        }

        // Final offline fallback.
        return try {
            offline.handle(clean)
        } catch (_: Exception) {
            "MONU could not complete this request while offline."
        }
    }
}
