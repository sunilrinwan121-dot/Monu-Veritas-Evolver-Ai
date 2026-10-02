package com.monu.mobile.domain.model

data class MONUTransferChunk(
    val transferId: String,
    val index: Int,
    val startByte: Long,
    val endByte: Long,
    val status: MONUTransferStatus
)

data class MONUResumableTransferState(
    val transferId: String,
    val fileName: String,
    val totalBytes: Long?,
    val completedBytes: Long,
    val completedChunks: Set<Int> = emptySet(),
    val resumable: Boolean = true,
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
