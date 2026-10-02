package com.monu.mobile.feature.transfer

import com.monu.mobile.domain.model.MONUFileTransfer
import com.monu.mobile.domain.model.MONUTransferDirection
import com.monu.mobile.domain.model.MONUTransferProgress
import com.monu.mobile.domain.model.MONUTransferStatus

class MONUTransferEngine {

    private val transfers = mutableListOf<MONUFileTransfer>()

    fun createTransfer(
        direction: MONUTransferDirection,
        fileName: String,
        sourceUri: String? = null,
        destinationUri: String? = null
    ): MONUFileTransfer {

        val transfer = MONUFileTransfer(
            id = "transfer_${System.currentTimeMillis()}",
            direction = direction,
            fileName = fileName,
            sourceUri = sourceUri,
            destinationUri = destinationUri,
            status = MONUTransferStatus.CREATED
        )

        transfers.add(transfer)

        return transfer
    }

    fun getTransfers(): List<MONUFileTransfer> {
        return transfers
            .sortedByDescending { it.createdAt }
    }

    fun updateStatus(
        transferId: String,
        status: MONUTransferStatus,
        error: String? = null
    ) {
        val index = transfers.indexOfFirst {
            it.id == transferId
        }

        if (index >= 0) {
            val old = transfers[index]

            transfers[index] = old.copy(
                status = status,
                error = error ?: old.error
            )
        }
    }

    fun updateProgress(
        transferId: String,
        progress: MONUTransferProgress
    ) {
        val index = transfers.indexOfFirst {
            it.id == transferId
        }

        if (index >= 0) {
            val old = transfers[index]

            transfers[index] = old.copy(
                progress = progress
            )
        }
    }

    fun pause(
        transferId: String
    ) {
        updateStatus(
            transferId,
            MONUTransferStatus.PAUSED
        )
    }

    fun resume(
        transferId: String
    ) {
        updateStatus(
            transferId,
            MONUTransferStatus.QUEUED
        )
    }

    fun cancel(
        transferId: String
    ) {
        updateStatus(
            transferId,
            MONUTransferStatus.CANCELLED
        )
    }
}
