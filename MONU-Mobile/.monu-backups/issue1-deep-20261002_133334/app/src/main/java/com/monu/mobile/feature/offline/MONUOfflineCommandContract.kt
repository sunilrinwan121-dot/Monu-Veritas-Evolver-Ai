package com.monu.mobile.feature.offline

data class MONUOfflineCommandRequest(
    val command: String
)

data class MONUOfflineCommandResponse(
    val handled: Boolean,
    val intent: MONUOfflineCommandIntent,
    val response: String
)

interface MONUOfflineCommandContract {

    fun canHandle(
        request: MONUOfflineCommandRequest
    ): Boolean

    fun execute(
        request: MONUOfflineCommandRequest
    ): MONUOfflineCommandResponse
}
