package com.monu.mobile.feature.health

import android.content.Context
import android.os.StatFs
import com.monu.mobile.domain.model.MONUHealthMetric
import com.monu.mobile.domain.model.MONUHealthStatus
import com.monu.mobile.domain.model.MONUSystemHealthReport

class MONUSystemHealthEngine(
    private val context: Context
) {

    fun inspect(): MONUSystemHealthReport {

        val metrics = mutableListOf<MONUHealthMetric>()

        val filesDir = context.filesDir
        val stat = StatFs(filesDir.absolutePath)

        val availableBytes =
            stat.availableBlocksLong * stat.blockSizeLong

        val totalBytes =
            stat.blockCountLong * stat.blockSizeLong

        val freePercent =
            if (totalBytes > 0) {
                (availableBytes * 100 / totalBytes)
            } else {
                0
            }

        val storageStatus =
            when {
                freePercent > 20 -> MONUHealthStatus.HEALTHY
                freePercent > 10 -> MONUHealthStatus.WARNING
                else -> MONUHealthStatus.CRITICAL
            }

        metrics += MONUHealthMetric(
            id = "storage",
            title = "Application Storage",
            description = "Real application storage availability.",
            status = storageStatus,
            value = "$freePercent% free"
        )

        metrics += MONUHealthMetric(
            id = "application",
            title = "MONU Application",
            description = "APK process is currently running.",
            status = MONUHealthStatus.HEALTHY,
            value = "RUNNING"
        )

        metrics += MONUHealthMetric(
            id = "server",
            title = "Server Connectivity",
            description =
                "Server truth is determined by the real connection engine.",
            status = MONUHealthStatus.UNKNOWN,
            value = "CHECK CONNECTION CENTER"
        )

        metrics += MONUHealthMetric(
            id = "security",
            title = "Security Authority",
            description =
                "Android capability limits apply. Kernel authority is not assumed.",
            status = MONUHealthStatus.UNKNOWN,
            value = "ANDROID SANDBOX"
        )

        val overall =
            when {
                metrics.any {
                    it.status == MONUHealthStatus.CRITICAL
                } -> MONUHealthStatus.CRITICAL

                metrics.any {
                    it.status == MONUHealthStatus.WARNING
                } -> MONUHealthStatus.WARNING

                metrics.all {
                    it.status == MONUHealthStatus.HEALTHY
                } -> MONUHealthStatus.HEALTHY

                else -> MONUHealthStatus.UNKNOWN
            }

        return MONUSystemHealthReport(
            generatedAt = System.currentTimeMillis(),
            overallStatus = overall,
            metrics = metrics
        )
    }
}
