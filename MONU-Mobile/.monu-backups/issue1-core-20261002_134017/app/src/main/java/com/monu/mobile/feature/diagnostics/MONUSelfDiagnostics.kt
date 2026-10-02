package com.monu.mobile.feature.diagnostics

import android.content.Context
import com.monu.mobile.domain.model.MONUDiagnosticCategory
import com.monu.mobile.domain.model.MONUDiagnosticReport
import com.monu.mobile.domain.model.MONUDiagnosticResult
import com.monu.mobile.domain.model.MONUDiagnosticStatus
import java.io.File

class MONUSelfDiagnostics(
    private val context: Context
) {

    fun runDiagnostics(): MONUDiagnosticReport {

        val started = System.currentTimeMillis()

        val results = mutableListOf<MONUDiagnosticResult>()

        results += checkApplication()
        results += checkStorage()
        results += checkDatabaseDirectory()
        results += checkNetworkPermission()
        results += checkServerConfiguration()

        return MONUDiagnosticReport(
            startedAt = started,
            completedAt = System.currentTimeMillis(),
            results = results
        )
    }

    private fun checkApplication(): MONUDiagnosticResult {
        return MONUDiagnosticResult(
            id = "application",
            category = MONUDiagnosticCategory.APPLICATION,
            title = "Application Runtime",
            description =
                "MONU self-diagnostic engine executed successfully.",
            status = MONUDiagnosticStatus.PASS
        )
    }

    private fun checkStorage(): MONUDiagnosticResult {

        val writable =
            try {
                val testFile =
                    File(context.cacheDir, "monu_diagnostic_test.tmp")

                testFile.writeText("MONU")
                val readable = testFile.readText() == "MONU"
                testFile.delete()

                readable
            } catch (_: Exception) {
                false
            }

        return MONUDiagnosticResult(
            id = "storage",
            category = MONUDiagnosticCategory.STORAGE,
            title = "Local Storage Access",
            description =
                if (writable) {
                    "Application storage read/write test passed."
                } else {
                    "Application storage test failed."
                },
            status =
                if (writable) {
                    MONUDiagnosticStatus.PASS
                } else {
                    MONUDiagnosticStatus.FAIL
                }
        )
    }

    private fun checkDatabaseDirectory(): MONUDiagnosticResult {

        val databaseDirectory =
            context.getDatabasePath("monu_database").parentFile

        val available =
            databaseDirectory?.exists()
                ?: false

        return MONUDiagnosticResult(
            id = "database",
            category = MONUDiagnosticCategory.DATABASE,
            title = "Database Environment",
            description =
                if (available) {
                    "Android database directory is available."
                } else {
                    "Database directory has not yet been created."
                },
            status =
                if (available) {
                    MONUDiagnosticStatus.PASS
                } else {
                    MONUDiagnosticStatus.WARNING
                }
        )
    }

    private fun checkNetworkPermission(): MONUDiagnosticResult {

        return MONUDiagnosticResult(
            id = "network",
            category = MONUDiagnosticCategory.NETWORK,
            title = "Internet Capability",
            description =
                "Manifest permission must be combined with a real network test.",
            status = MONUDiagnosticStatus.UNKNOWN
        )
    }

    private fun checkServerConfiguration(): MONUDiagnosticResult {

        return MONUDiagnosticResult(
            id = "server",
            category = MONUDiagnosticCategory.SERVER,
            title = "Server Configuration",
            description =
                "Configuration existence does not mean server connectivity.",
            status = MONUDiagnosticStatus.UNKNOWN
        )
    }
}
