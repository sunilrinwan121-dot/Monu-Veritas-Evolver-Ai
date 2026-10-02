package com.monu.mobile.feature.sync

import com.monu.mobile.domain.model.StateSnapshot
import com.monu.mobile.domain.model.SyncReport
import com.monu.mobile.domain.model.SyncRequest
import com.monu.mobile.domain.model.SyncResult
import com.monu.mobile.domain.model.SyncStatus

class MONUStateSynchronizationEngine {

    private val states = mutableMapOf<String, StateSnapshot>()
    private val results = mutableListOf<SyncResult>()

    fun updateLocalState(
        snapshot: StateSnapshot
    ): StateSnapshot {

        states[snapshot.key] = snapshot

        return snapshot
    }

    fun getState(
        key: String
    ): StateSnapshot? {
        return states[key]
    }

    fun allStates(): List<StateSnapshot> {
        return states.values.sortedBy {
            it.key
        }
    }

    fun requestSync(
        request: SyncRequest
    ): SyncResult {

        val availableKeys = request.stateKeys.filter {
            states.containsKey(it)
        }

        val missingKeys = request.stateKeys.filter {
            !states.containsKey(it)
        }

        val status = when {
            missingKeys.isEmpty() ->
                SyncStatus.SYNCHRONIZED

            availableKeys.isNotEmpty() ->
                SyncStatus.UNKNOWN

            else ->
                SyncStatus.FAILED
        }

        val result = SyncResult(
            syncId = request.syncId,
            status = status,
            synchronizedKeys = availableKeys,
            conflicts = missingKeys,
            message = when (status) {
                SyncStatus.SYNCHRONIZED ->
                    "Local state prepared for synchronization"
                SyncStatus.UNKNOWN ->
                    "Partial state availability"
                else ->
                    "Requested state unavailable"
            },
            completedAt = System.currentTimeMillis()
        )

        results.removeAll {
            it.syncId == request.syncId
        }

        results += result

        return result
    }

    fun getResults(): List<SyncResult> {
        return results.toList()
    }

    fun report(): SyncReport {
        return SyncReport(
            results = results.toList(),
            total = results.size,
            synchronized = results.count {
                it.status == SyncStatus.SYNCHRONIZED
            },
            conflicts = results.count {
                it.status == SyncStatus.CONFLICT
            },
            failed = results.count {
                it.status == SyncStatus.FAILED
            },
            unknown = results.count {
                it.status == SyncStatus.UNKNOWN
            }
        )
    }
}
