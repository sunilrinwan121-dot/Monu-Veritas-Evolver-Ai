
package com.monu.mobile.feature.workflows

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Data
import java.util.concurrent.TimeUnit

object MONUWorkflowScheduler {

    fun ensureScheduled(context: Context) {
        val request =
            PeriodicWorkRequestBuilder<MONUWorkflowWorker>(
                24,
                TimeUnit.HOURS
            )
                .setInputData(
                    Data.Builder()
                        .putString(
                            MONUWorkflowWorker.KEY_WORKFLOW_ID,
                            "daily_system_review"
                        )
                        .build()
                )
                .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(
                MONUWorkflowWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}
