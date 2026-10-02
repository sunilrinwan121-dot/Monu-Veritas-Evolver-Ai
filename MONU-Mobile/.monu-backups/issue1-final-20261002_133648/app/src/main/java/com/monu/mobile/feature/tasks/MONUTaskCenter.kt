package com.monu.mobile.feature.tasks

import com.monu.mobile.domain.model.MONUTask
import com.monu.mobile.domain.model.MONUTaskStatus

class MONUTaskCenter {

    private val tasks = mutableListOf<MONUTask>()

    fun addTask(task: MONUTask) {
        tasks.add(0, task)
    }

    fun allTasks(): List<MONUTask> {
        return tasks.toList()
    }

    fun activeTasks(): List<MONUTask> {
        return tasks.filter {
            it.status in setOf(
                MONUTaskStatus.QUEUED,
                MONUTaskStatus.STARTING,
                MONUTaskStatus.RUNNING,
                MONUTaskStatus.PROCESSING,
                MONUTaskStatus.VERIFYING,
                MONUTaskStatus.RECOVERING,
                MONUTaskStatus.RETRYING
            )
        }
    }

    fun completedTasks(): List<MONUTask> {
        return tasks.filter {
            it.status == MONUTaskStatus.COMPLETED
        }
    }

    fun failedTasks(): List<MONUTask> {
        return tasks.filter {
            it.status == MONUTaskStatus.FAILED
        }
    }
}
