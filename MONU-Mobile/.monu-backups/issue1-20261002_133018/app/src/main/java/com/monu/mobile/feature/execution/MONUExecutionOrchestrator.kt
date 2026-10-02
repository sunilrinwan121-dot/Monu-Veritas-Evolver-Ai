package com.monu.mobile.feature.execution

import com.monu.mobile.domain.model.ExecutionRecord
import com.monu.mobile.domain.model.ExecutionReport
import com.monu.mobile.domain.model.ExecutionRequest
import com.monu.mobile.domain.model.ExecutionStatus
import com.monu.mobile.domain.model.ExecutionType

class MONUExecutionOrchestrator {

    private val executions = mutableListOf<ExecutionRecord>()

    fun register(request: ExecutionRequest): ExecutionRecord {

        val record = ExecutionRecord(
            executionId = request.executionId,
            type = request.type,
            title = request.title,
            status = ExecutionStatus.CREATED
        )

        executions.removeAll {
            it.executionId == request.executionId
        }

        executions += record

        return record
    }

    fun updateStatus(
        executionId: String,
        status: ExecutionStatus,
        error: String? = null
    ): ExecutionRecord? {

        val index = executions.indexOfFirst {
            it.executionId == executionId
        }

        if (index == -1) return null

        val current = executions[index]

        val updated = current.copy(
            status = status,
            startedAt = if (
                status == ExecutionStatus.RUNNING &&
                current.startedAt == null
            ) {
                System.currentTimeMillis()
            } else {
                current.startedAt
            },
            completedAt = if (
                status == ExecutionStatus.SUCCEEDED ||
                status == ExecutionStatus.FAILED ||
                status == ExecutionStatus.CANCELLED
            ) {
                System.currentTimeMillis()
            } else {
                current.completedAt
            },
            error = error
        )

        executions[index] = updated

        return updated
    }

    fun getExecution(
        executionId: String
    ): ExecutionRecord? {
        return executions.firstOrNull {
            it.executionId == executionId
        }
    }

    fun getAll(): List<ExecutionRecord> {
        return executions.toList()
    }

    fun report(): ExecutionReport {

        return ExecutionReport(
            executions = executions.toList(),
            total = executions.size,
            running = executions.count {
                it.status == ExecutionStatus.RUNNING
            },
            succeeded = executions.count {
                it.status == ExecutionStatus.SUCCEEDED
            },
            failed = executions.count {
                it.status == ExecutionStatus.FAILED
            },
            unknown = executions.count {
                it.status == ExecutionStatus.UNKNOWN
            }
        )
    }

    fun createUnknownExecution(
        title: String,
        type: ExecutionType = ExecutionType.SYSTEM_ACTION
    ): ExecutionRecord {

        val id = "unknown-${System.currentTimeMillis()}"

        return register(
            ExecutionRequest(
                executionId = id,
                type = type,
                title = title
            )
        ).copy(
            status = ExecutionStatus.UNKNOWN
        ).also { updated ->

            val index = executions.indexOfFirst {
                it.executionId == updated.executionId
            }

            if (index >= 0) {
                executions[index] = updated
            }
        }
    }
}
