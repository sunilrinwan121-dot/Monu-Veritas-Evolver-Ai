package com.monu.mobile.domain.model

enum class MONUTransferDirection {
    UPLOAD,
    DOWNLOAD
}

enum class MONUTransferStatus {
    CREATED,
    QUEUED,
    PREPARING,
    RUNNING,
    PAUSED,
    RETRYING,
    VERIFYING,
    COMPLETED,
    FAILED,
    CANCELLED,
    UNKNOWN
}

data class MONUTransferProgress(
    val transferredBytes: Long,
    val totalBytes: Long?,
    val percentage: Int?,
    val speedBytesPerSecond: Long? = null,
    val estimatedSecondsRemaining: Long? = null
)

data class MONUFileTransfer(
    val id: String,
    val direction: MONUTransferDirection,
    val fileName: String,
    val sourceUri: String? = null,
    val destinationUri: String? = null,
    val status: MONUTransferStatus,
    val progress: MONUTransferProgress? = null,
    val error: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
