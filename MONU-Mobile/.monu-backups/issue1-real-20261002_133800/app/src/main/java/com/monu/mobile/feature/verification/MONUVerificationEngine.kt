package com.monu.mobile.feature.verification

import com.monu.mobile.domain.model.VerificationEvidence
import com.monu.mobile.domain.model.VerificationReport
import com.monu.mobile.domain.model.VerificationRequest
import com.monu.mobile.domain.model.VerificationResult
import com.monu.mobile.domain.model.VerificationStatus

class MONUVerificationEngine {

    private val results = mutableListOf<VerificationResult>()

    fun begin(
        request: VerificationRequest
    ): VerificationResult {

        val result = VerificationResult(
            verificationId = request.verificationId,
            executionId = request.executionId,
            status = VerificationStatus.PENDING,
            message = "Verification requested"
        )

        results.removeAll {
            it.verificationId == request.verificationId
        }

        results += result

        return result
    }

    fun verify(
        verificationId: String,
        evidence: List<VerificationEvidence>,
        message: String
    ): VerificationResult? {

        val index = results.indexOfFirst {
            it.verificationId == verificationId
        }

        if (index == -1) return null

        val trustedEvidence = evidence.filter {
            it.trusted
        }

        val status = when {
            trustedEvidence.isNotEmpty() ->
                VerificationStatus.VERIFIED

            evidence.isEmpty() ->
                VerificationStatus.INCONCLUSIVE

            else ->
                VerificationStatus.INCONCLUSIVE
        }

        val updated = results[index].copy(
            status = status,
            evidence = evidence,
            message = message,
            verifiedAt = System.currentTimeMillis()
        )

        results[index] = updated

        return updated
    }

    fun reject(
        verificationId: String,
        message: String
    ): VerificationResult? {

        val index = results.indexOfFirst {
            it.verificationId == verificationId
        }

        if (index == -1) return null

        val updated = results[index].copy(
            status = VerificationStatus.REJECTED,
            message = message,
            verifiedAt = System.currentTimeMillis()
        )

        results[index] = updated

        return updated
    }

    fun get(
        verificationId: String
    ): VerificationResult? {
        return results.firstOrNull {
            it.verificationId == verificationId
        }
    }

    fun all(): List<VerificationResult> {
        return results.toList()
    }

    fun report(): VerificationReport {

        return VerificationReport(
            results = results.toList(),
            total = results.size,
            verified = results.count {
                it.status == VerificationStatus.VERIFIED
            },
            rejected = results.count {
                it.status == VerificationStatus.REJECTED
            },
            inconclusive = results.count {
                it.status == VerificationStatus.INCONCLUSIVE
            },
            unknown = results.count {
                it.status == VerificationStatus.UNKNOWN
            }
        )
    }
}
