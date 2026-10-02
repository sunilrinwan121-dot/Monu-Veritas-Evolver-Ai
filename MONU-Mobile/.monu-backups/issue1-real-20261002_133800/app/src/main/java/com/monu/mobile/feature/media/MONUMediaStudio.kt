package com.monu.mobile.feature.media

import com.monu.mobile.domain.model.MONUMediaAsset
import com.monu.mobile.domain.model.MONUMediaJob
import com.monu.mobile.domain.model.MONUMediaJobStatus
import com.monu.mobile.domain.model.MONUMediaOperation

class MONUMediaStudio {

    private val jobs = mutableListOf<MONUMediaJob>()

    fun createJob(
        operation: MONUMediaOperation,
        assets: List<MONUMediaAsset>
    ): MONUMediaJob {

        val job = MONUMediaJob(
            id = "media_${System.currentTimeMillis()}",
            operation = operation,
            status = MONUMediaJobStatus.DRAFT,
            inputAssets = assets
        )

        jobs.add(job)

        return job
    }

    fun getJobs(): List<MONUMediaJob> {
        return jobs
            .sortedByDescending { it.createdAt }
    }

    fun getJob(
        jobId: String
    ): MONUMediaJob? {
        return jobs.find { it.id == jobId }
    }

    fun updateStatus(
        jobId: String,
        status: MONUMediaJobStatus,
        progress: Int? = null,
        stage: String? = null,
        error: String? = null
    ) {
        val index = jobs.indexOfFirst {
            it.id == jobId
        }

        if (index >= 0) {
            val old = jobs[index]

            jobs[index] = old.copy(
                status = status,
                progress = progress ?: old.progress,
                currentStage = stage ?: old.currentStage,
                error = error ?: old.error
            )
        }
    }
}
