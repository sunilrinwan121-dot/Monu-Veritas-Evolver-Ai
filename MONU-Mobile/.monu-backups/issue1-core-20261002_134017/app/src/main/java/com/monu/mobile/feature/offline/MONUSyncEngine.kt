package com.monu.mobile.feature.offline

import com.monu.mobile.domain.repository.ConnectionRepository

class MONUSyncEngine(
    private val queue:
        OfflineCommandQueue,
    private val connectionRepository:
        ConnectionRepository =
            ConnectionRepository()
) {

    /*
     * This engine intentionally does NOT
     * pretend to send commands.
     *
     * Real command API integration will be
     * added only after the MONU Server
     * command contract is verified.
     */

    suspend fun synchronize(): SyncResult {

        val connection =
            connectionRepository
                .checkConnection()

        if (
            connection.apkToServer.name
            != "CONNECTED"
        ) {
            return SyncResult(
                attempted = 0,
                acknowledged = 0,
                message =
                    "Server is not connected. Queue preserved."
            )
        }

        val pending =
            queue.pending()

        return SyncResult(
            attempted = pending.size,
            acknowledged = 0,
            message =
                "Server connected, but command endpoint is not configured. Queue preserved."
        )
    }
}

data class SyncResult(
    val attempted: Int,
    val acknowledged: Int,
    val message: String
)
