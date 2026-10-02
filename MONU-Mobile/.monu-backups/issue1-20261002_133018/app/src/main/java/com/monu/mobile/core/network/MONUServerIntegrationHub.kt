package com.monu.mobile.core.network

import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Extensible Server Integration & REST Gateway (Fix for Point 11)
 * Bridges UI capabilities with real backend APIs and microservices.
 */
class MONUServerIntegrationHub {

    suspend fun executeBackendApiCall(
        endpointUrl: String,
        httpMethod: String = "POST",
        payloadJson: JSONObject? = null,
        authToken: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = URL(endpointUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = httpMethod
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            authToken?.let {
                connection.setRequestProperty("Authorization", "Bearer $it")
            }

            if (payloadJson != null && (httpMethod == "POST" || httpMethod == "PUT")) {
                connection.doOutput = true
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(payloadJson.toString())
                    writer.flush()
                }
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                Result.success(responseText)
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "HTTP Error $responseCode"
                Result.failure(Exception("Server returned status $responseCode: $errorText"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
