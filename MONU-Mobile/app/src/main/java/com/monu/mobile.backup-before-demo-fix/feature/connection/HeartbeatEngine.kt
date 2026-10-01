package com.monu.mobile.feature.connection

import com.monu.mobile.domain.model.ConnectionStatus
import com.monu.mobile.domain.repository.ConnectionRepository
import kotlinx.coroutines.*

class HeartbeatEngine(
    private val repository: ConnectionRepository = ConnectionRepository()
) {

    companion object {
        const val HEARTBEAT_INTERVAL_MS = 5 * 60 * 1000L
    }

    private var job: Job? = null

    fun start(
        scope: CoroutineScope,
        onStatus: (ConnectionStatus) -> Unit
    ) {

        stop()

        job = scope.launch(Dispatchers.IO) {

            while (isActive) {

                val status =
                    repository.checkConnection()

                withContext(Dispatchers.Main) {
                    onStatus(status)
                }

                delay(HEARTBEAT_INTERVAL_MS)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}
