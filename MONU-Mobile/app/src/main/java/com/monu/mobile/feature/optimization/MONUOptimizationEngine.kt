package com.monu.mobile.feature.optimization

import com.monu.mobile.domain.model.OptimizationConfidence
import com.monu.mobile.domain.model.OptimizationOpportunity
import com.monu.mobile.domain.model.OptimizationRecommendation

class MONUOptimizationEngine {

    fun analyze(
        opportunities: List<OptimizationOpportunity>
    ): List<OptimizationOpportunity> {
        return opportunities.filter {
            it.id.isNotBlank() &&
            it.title.isNotBlank() &&
            it.description.isNotBlank()
        }
    }

    fun recommendations(
        opportunities: List<OptimizationOpportunity>
    ): List<OptimizationRecommendation> {
        return analyze(opportunities).map { opportunity ->
            OptimizationRecommendation(
                id = "recommendation-${opportunity.id}",
                opportunityId = opportunity.id,
                recommendation = buildRecommendation(opportunity),
                confidence = when (opportunity.confidence) {
                    com.monu.mobile.domain.model.OptimizationConfidence.HIGH ->
                        OptimizationConfidence.HIGH
                    com.monu.mobile.domain.model.OptimizationConfidence.MEDIUM ->
                        OptimizationConfidence.MEDIUM
                    com.monu.mobile.domain.model.OptimizationConfidence.LOW ->
                        OptimizationConfidence.LOW
                    OptimizationConfidence.UNKNOWN ->
                        OptimizationConfidence.LOW
                },
                applied = false
            )
        }
    }

    private fun buildRecommendation(
        opportunity: OptimizationOpportunity
    ): String {
        val source = opportunity.source?.takeIf { it.isNotBlank() }
            ?: "verified opportunity source"

        return "Review '${opportunity.title}' using the verified source $source before applying an optimization."
    }
}
