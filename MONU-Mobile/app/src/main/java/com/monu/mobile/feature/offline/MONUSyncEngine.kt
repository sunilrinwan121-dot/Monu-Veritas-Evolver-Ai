package com.monu.mobile.feature.offline

import com.monu.mobile.domain.repository.ConnectionRepository

class MONUSyncEngine(
    private val queue: OfflineCommandQueue,
    private val connectionRepository: ConnectionRepository
) {

    suspend fun synchronize(): SyncResult {
        val connection =
            connectionRepository.checkConnection()

        if (connection.apkToServer.name != "CONNECTED") {
            return SyncResult(
                attempted = 0,
                acknowledged = 0,
                message = "Server is not connected. Queue preserved."
            )
        }

        val pending = queue.pending()

        var acknowledged = 0

        for (item in pending) {
            val result =
                connectionRepository.sendCommand(
                    command = item.command
                )

            if (result.success) {
                acknowledged++
            } else {
                break
            }
        }

        return SyncResult(
            attempted = pending.size,
            acknowledged = acknowledged,
            message =
                if (acknowledged == pending.size) {
                    "All pending commands were acknowledged by the server."
                } else {
                    "Server synchronization stopped after the first failed command."
                }
        )
    }
}

data class SyncResult(
    val attempted: Int,
    val acknowledged: Int,
    val message: String
)
