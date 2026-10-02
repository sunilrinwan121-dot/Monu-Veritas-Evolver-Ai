package com.monu.mobile.feature.conversation

object MONUUserFacingErrorMapper {

    fun map(error: Throwable): String {
        return when (error) {
            is java.net.UnknownHostException ->
                "No internet connection or the requested service could not be reached."

            is java.net.SocketTimeoutException ->
                "The request took too long to complete. Please check your connection and try again."

            is java.io.IOException ->
                "A network error prevented MONU from completing the request."

            is SecurityException ->
                "MONU does not have permission required to complete this request."

            else ->
                "MONU could not complete this request. Please try again."
        }
    }
}
