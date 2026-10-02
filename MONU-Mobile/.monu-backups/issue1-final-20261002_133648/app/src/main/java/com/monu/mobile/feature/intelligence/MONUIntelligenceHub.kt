package com.monu.mobile.feature.intelligence

import com.monu.mobile.feature.context.MONUContextIntelligence
import com.monu.mobile.feature.decision.MONUDecisionCenter
import com.monu.mobile.feature.gemini.MONUGeminiIntelligenceEngine
import com.monu.mobile.feature.knowledge.MONUKnowledgeCenter
import com.monu.mobile.feature.planning.MONUPlanningIntelligence
import com.monu.mobile.feature.rules.MONURulesEngine
import com.monu.mobile.feature.verification.MONUVerificationEngine

data class MONUIntelligenceCapability(
    val name: String,
    val available: Boolean,
    val description: String
)

class MONUIntelligenceHub(
    private val gemini: MONUGeminiIntelligenceEngine =
        MONUGeminiIntelligenceEngine()
) {

    private val unifiedIntelligence =
        MONUUnifiedIntelligence()

    private val decisionCenter =
        MONUDecisionCenter()

    private val planningIntelligence =
        MONUPlanningIntelligence()

    private val contextIntelligence =
        MONUContextIntelligence()

    private val knowledgeCenter =
        MONUKnowledgeCenter()

    private val verificationEngine =
        MONUVerificationEngine()

    private val rulesEngine =
        MONURulesEngine()

    // Shared Gemini provider supplied by MONUMasterBrain.

    fun capabilities(): List<MONUIntelligenceCapability> {

        val capabilities = mutableListOf<MONUIntelligenceCapability>()

        capabilities += MONUIntelligenceCapability(
            name = "Unified Intelligence",
            available = runCatching {
                unifiedIntelligence.snapshot()
            }.isSuccess,
            description =
                "Collects signals, analyzes insights and creates intelligence snapshots."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Decision Center",
            available = runCatching {
                decisionCenter
            }.isSuccess,
            description =
                "Supports structured option and decision management."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Planning Intelligence",
            available = runCatching {
                planningIntelligence
            }.isSuccess,
            description =
                "Creates plans and determines executable next steps."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Context Intelligence",
            available = runCatching {
                contextIntelligence.realContext()
            }.getOrDefault(emptyList()).isNotEmpty(),
            description =
                "Stores, prioritizes and snapshots contextual information."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Knowledge Center",
            available = runCatching {
                knowledgeCenter.realKnowledge()
            }.getOrDefault(emptyList()).isNotEmpty(),
            description =
                "Searches internal knowledge structures."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Verification Engine",
            available = runCatching {
                verificationEngine.report()
            }.isSuccess,
            description =
                "Tracks evidence and verification results."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Rules Engine",
            available = runCatching {
                rulesEngine.realRules()
            }.getOrDefault(emptyList()).isNotEmpty(),
            description =
                "Evaluates structured MONU rules."
        )

        capabilities += MONUIntelligenceCapability(
            name = "Gemini Intelligence",
            available = gemini.isConfigured(),
            description =
                if (gemini.isConfigured()) {
                    "Cloud AI reasoning is configured."
                } else {
                    "Gemini engine exists but API key is not configured."
                }
        )

        return capabilities
    }

    fun healthReport(): String {

        return buildString {

            append("MONU INTELLIGENCE HUB\n\n")

            capabilities().forEach {

                append(
                    if (it.available) "✓ " else "○ "
                )

                append(it.name)
                append("\n")

                append(it.description)
                append("\n\n")
            }
        }.trim()
    }
}
