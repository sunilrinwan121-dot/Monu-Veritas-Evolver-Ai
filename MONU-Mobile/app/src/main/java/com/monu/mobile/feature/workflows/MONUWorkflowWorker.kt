
package com.monu.mobile.feature.workflows

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MONUWorkflowWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val workflowId = inputData.getString(KEY_WORKFLOW_ID)
            ?: return Result.failure()

        val run = MONUWorkflowCenter(applicationContext)
            .execute(workflowId)

        return if (
            run.status ==
            com.monu.mobile.domain.model.MONUWorkflowStatus.COMPLETED
        ) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val KEY_WORKFLOW_ID = "workflow_id"
        const val WORK_NAME = "monu_daily_system_review"
    }
}
