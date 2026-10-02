package com.monu.mobile.feature.offline

class MONUOfflineCommandRouter : MONUOfflineCommandContract {

    private val intentParser = MONUOfflineCommandIntentParser()
    private val localDeviceCommandEngine = MONULocalDeviceCommandEngine()

    fun masterCapabilities(): List<String> = listOf(
        "Empty command detection",
        "Greeting handling",
        "Help command handling",
        "Local runtime status",
        "MONU identity response",
        "Current time retrieval",
        "Current date retrieval",
        "Local device status"
    )

    override fun canHandle(
        request: MONUOfflineCommandRequest
    ): Boolean = canHandle(request.command)

    fun canHandle(command: String): Boolean =
        intentParser.parse(command) != MONUOfflineCommandIntent.UNKNOWN

    fun handle(command: String): String =
        when (intentParser.parse(command)) {
            MONUOfflineCommandIntent.EMPTY ->
                "Please say or type a command."
            MONUOfflineCommandIntent.GREETING ->
                "Hello. MONU is running locally in offline mode."
            MONUOfflineCommandIntent.HELP ->
                masterCapabilities().joinToString(
                    prefix = "MONU offline capabilities:\n• ",
                    separator = "\n• "
                )
            MONUOfflineCommandIntent.STATUS ->
                "MONU local runtime is ready. Offline command system is active."
            MONUOfflineCommandIntent.IDENTITY ->
                "I am MONU, your personal AI assistant."
            MONUOfflineCommandIntent.TIME,
            MONUOfflineCommandIntent.DATE,
            MONUOfflineCommandIntent.LOCAL_STATUS ->
                localDeviceCommandEngine.handle(command)
            MONUOfflineCommandIntent.UNKNOWN ->
                "This command requires a verified local capability and cannot be executed offline."
        }

    override fun execute(
        request: MONUOfflineCommandRequest
    ): MONUOfflineCommandResponse {
        val intent = intentParser.parse(request.command)
        return MONUOfflineCommandResponse(
            handled = intent != MONUOfflineCommandIntent.UNKNOWN,
            intent = intent,
            response = handle(request.command)
        )
    }
}
