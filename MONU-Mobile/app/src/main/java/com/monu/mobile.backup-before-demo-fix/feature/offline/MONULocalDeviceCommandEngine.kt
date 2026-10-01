package com.monu.mobile.feature.offline

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MONULocalDeviceCommandEngine {

    fun canHandle(command: String): Boolean {
        val normalized = command.trim().lowercase()

        return normalized.contains("time") ||
            normalized.contains("date") ||
            normalized.contains("today") ||
            normalized.contains("device status") ||
            normalized.contains("local status")
    }

    fun handle(command: String): String {
        val normalized = command.trim().lowercase()

        return when {
            normalized.contains("time") ->
                currentTime()

            normalized.contains("date") ||
                normalized.contains("today") ->
                currentDate()

            normalized.contains("device status") ||
                normalized.contains("local status") ->
                localRuntimeStatus()

            else ->
                "This local device command is not available."
        }
    }

    private fun currentTime(): String {
        val formatter =
            SimpleDateFormat("hh:mm a", Locale.getDefault())

        return "Current local time: ${formatter.format(Date())}"
    }

    private fun currentDate(): String {
        val formatter =
            SimpleDateFormat(
                "EEEE, dd MMMM yyyy",
                Locale.getDefault()
            )

        return "Today's date: ${formatter.format(Date())}"
    }

    private fun localRuntimeStatus(): String {
        return "MONU local device command engine is ready."
    }
}
