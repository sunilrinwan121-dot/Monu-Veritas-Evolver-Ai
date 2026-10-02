package com.monu.mobile.feature.offline

data class MONUOfflineCommandCapability(
    val intent: MONUOfflineCommandIntent,
    val executionOwner: String,
    val description: String
)

class MONUOfflineCommandCapabilityMatrix {

    fun capabilities(): List<MONUOfflineCommandCapability> {
        return listOf(
            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.EMPTY,
                executionOwner = "Router",
                description = "Handles blank commands safely."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.GREETING,
                executionOwner = "Router",
                description = "Handles local greetings."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.HELP,
                executionOwner = "Router",
                description = "Provides available offline commands."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.STATUS,
                executionOwner = "Router",
                description = "Reports MONU offline runtime status."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.IDENTITY,
                executionOwner = "Router",
                description = "Explains MONU identity."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.TIME,
                executionOwner = "LocalDeviceEngine",
                description = "Reads current local device time."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.DATE,
                executionOwner = "LocalDeviceEngine",
                description = "Reads current local device date."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.LOCAL_STATUS,
                executionOwner = "LocalDeviceEngine",
                description = "Reports local device command runtime status."
            ),

            MONUOfflineCommandCapability(
                intent = MONUOfflineCommandIntent.UNKNOWN,
                executionOwner = "Router",
                description = "Safely handles unsupported offline commands."
            )
        )
    }

    fun ownerFor(
        intent: MONUOfflineCommandIntent
    ): String? {
        return capabilities()
            .firstOrNull { it.intent == intent }
            ?.executionOwner
    }

    fun supports(
        intent: MONUOfflineCommandIntent
    ): Boolean {
        return capabilities()
            .any { it.intent == intent }
    }
}
