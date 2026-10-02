package com.monu.mobile.domain.model

enum class VerificationStatus {
    NOT_REQUESTED,
    PENDING,
    VERIFYING,
    VERIFIED,
    REJECTED,
    INCONCLUSIVE,
    UNKNOWN
}

enum class EvidenceType {
    LOCAL_RESULT,
    FILE_EXISTS,
    DATABASE_RECORD,
    NETWORK_RESPONSE,
    SERVER_ACKNOWLEDGEMENT,
    USER_CONFIRMATION,
    SYSTEM_STATE,
    CUSTOM
}

data class VerificationEvidence(
    val evidenceId: String,
    val type: EvidenceType,
    val description: String,
    val value: String? = null,
    val capturedAt: Long = System.currentTimeMillis(),
    val trusted: Boolean = false
)

data class VerificationRequest(
    val verificationId: String,
    val executionId: String,
    val title: String,
    val requestedAt: Long = System.currentTimeMillis()
)

data class VerificationResult(
    val verificationId: String,
    val executionId: String,
    val status: VerificationStatus,
    val evidence: List<VerificationEvidence> = emptyList(),
    val message: String,
    val verifiedAt: Long? = null
)

data class VerificationReport(
    val results: List<VerificationResult>,
    val total: Int,
    val verified: Int,
    val rejected: Int,
    val inconclusive: Int,
    val unknown: Int
)
