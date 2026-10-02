package com.monu.mobile.feature.offline

enum class MONUOfflineCommandIntent {
    EMPTY,
    GREETING,
    HELP,
    STATUS,
    IDENTITY,
    TIME,
    DATE,
    LOCAL_STATUS,
    UNKNOWN
}

class MONUOfflineCommandIntentParser {

    fun parse(command: String): MONUOfflineCommandIntent {
        val normalized = command.trim().lowercase()

        return when {
            normalized.isBlank() ->
                MONUOfflineCommandIntent.EMPTY

            normalized.contains("hello") ||
                normalized.contains("hi monu") ->
                MONUOfflineCommandIntent.GREETING

            normalized.contains("help") ||
                normalized.contains("what can you do") ->
                MONUOfflineCommandIntent.HELP

            normalized.contains("who are you") ->
                MONUOfflineCommandIntent.IDENTITY

            normalized.contains("device status") ||
                normalized.contains("local status") ->
                MONUOfflineCommandIntent.LOCAL_STATUS

            normalized.contains("status") ->
                MONUOfflineCommandIntent.STATUS

            normalized.contains("time") ->
                MONUOfflineCommandIntent.TIME

            normalized.contains("date") ||
                normalized.contains("today") ->
                MONUOfflineCommandIntent.DATE

            else ->
                MONUOfflineCommandIntent.UNKNOWN
        }
    }
}
