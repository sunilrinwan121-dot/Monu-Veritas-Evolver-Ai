package com.monu.mobile.feature.transfer

import com.monu.mobile.domain.model.MONUTransferChunk
import com.monu.mobile.domain.model.MONUTransferStatus

class MONUChunkPlanner {

    fun createChunks(
        transferId: String,
        totalBytes: Long,
        chunkSizeBytes: Long = 5L * 1024L * 1024L
    ): List<MONUTransferChunk> {

        if (totalBytes <= 0L) {
            return emptyList()
        }

        val chunks = mutableListOf<MONUTransferChunk>()

        var start = 0L
        var index = 0

        while (start < totalBytes) {

            val end = minOf(
                start + chunkSizeBytes - 1L,
                totalBytes - 1L
            )

            chunks.add(
                MONUTransferChunk(
                    transferId = transferId,
                    index = index,
                    startByte = start,
                    endByte = end,
                    status = MONUTransferStatus.CREATED
                )
            )

            start = end + 1L
            index++
        }

        return chunks
    }
}
