package com.monu.mobile.feature.offline

import android.content.Context
import com.monu.mobile.data.local.MONUDatabaseProvider
import com.monu.mobile.data.local.entity.OfflineCommandEntity
import com.monu.mobile.domain.model.CommandSyncState
import java.util.UUID

class OfflineCommandQueue(
    context: Context
) {

    private val dao =
        MONUDatabaseProvider
            .get(context)
            .offlineCommandDao()

    suspend fun enqueue(
        command: String
    ): String {

        val id =
            UUID.randomUUID().toString()

        dao.insert(
            OfflineCommandEntity(
                id = id,
                command = command,
                createdAt =
                    System.currentTimeMillis(),
                syncState =
                    CommandSyncState.PENDING.name
            )
        )

        return id
    }

    suspend fun pending():
        List<OfflineCommandEntity> {

        return dao.getPending()
    }

    suspend fun markSyncing(
        command:
            OfflineCommandEntity
    ) {
        dao.update(
            command.copy(
                syncState =
                    CommandSyncState.SYNCING.name
            )
        )
    }

    suspend fun markRetry(
        command:
            OfflineCommandEntity,
        error: String?
    ) {
        dao.update(
            command.copy(
                syncState =
                    CommandSyncState.RETRY_PENDING.name,
                retryCount =
                    command.retryCount + 1,
                lastError = error
            )
        )
    }

    suspend fun markAcknowledged(
        command:
            OfflineCommandEntity
    ) {
        dao.update(
            command.copy(
                syncState =
                    CommandSyncState.ACKNOWLEDGED.name
            )
        )
    }

    suspend fun count(): Int {
        return dao.count()
    }
}
