package com.monu.mobile.data.network

import android.content.Context
import com.monu.mobile.data.config.ServerConfigStore
import com.monu.mobile.domain.model.CapabilityStatus
import com.monu.mobile.domain.model.ConnectionState
import com.monu.mobile.domain.model.ConnectionStatus
import com.monu.mobile.domain.model.ServerEndpointConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

data class MONUServerResponse(
    val success: Boolean,
    val code: Int = 0,
    val body: String = "",
    val text: String = "",
    val error: String? = null
)

class MONUServerClient(
    context: Context
) {
    private val appContext = context.applicationContext
    private val configStore = ServerConfigStore(appContext)

    private val client =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    private val jsonMediaType =
        "application/json; charset=utf-8".toMediaType()

    private fun config(): ServerEndpointConfig =
        configStore.load()

    private fun normalizeBaseUrl(value: String): String =
        value.trim().trimEnd('/')

    private fun resolveUrl(
        baseUrl: String,
        path: String
    ): String {
        val base = normalizeBaseUrl(baseUrl)
        val cleanPath =
            path.trim().let {
                if (it.isBlank()) "" else "/" + it.trimStart('/')
            }

        return base + cleanPath
    }

    private fun get(
        url: String
    ): MONUServerResponse {
        if (url.isBlank()) {
            return MONUServerResponse(
                success = false,
                error = "Server endpoint is not configured."
            )
        }

        return try {
            val request =
                Request.Builder()
                    .url(url)
                    .get()
                    .build()

            client.newCall(request).execute().use { response ->
                val body =
                    response.body?.string().orEmpty()

                MONUServerResponse(
                    success = response.isSuccessful,
                    code = response.code,
                    body = body,
                    text = extractText(body),
                    error =
                        if (response.isSuccessful) null
                        else "HTTP ${response.code}"
                )
            }
        } catch (error: IOException) {
            MONUServerResponse(
                success = false,
                error = error.message ?: "Network connection failed."
            )
        } catch (error: Exception) {
            MONUServerResponse(
                success = false,
                error = error.message ?: "Server request failed."
            )
        }
    }

    private fun postJson(
        url: String,
        payload: JSONObject
    ): MONUServerResponse {
        if (url.isBlank()) {
            return MONUServerResponse(
                success = false,
                error = "Server endpoint is not configured."
            )
        }

        return try {
            val body =
                payload
                    .toString()
                    .toRequestBody(jsonMediaType)

            val request =
                Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

            client.newCall(request).execute().use { response ->
                val responseBody =
                    response.body?.string().orEmpty()

                MONUServerResponse(
                    success = response.isSuccessful,
                    code = response.code,
                    body = responseBody,
                    text = extractText(responseBody),
                    error =
                        if (response.isSuccessful) null
                        else "HTTP ${response.code}"
                )
            }
        } catch (error: IOException) {
            MONUServerResponse(
                success = false,
                error = error.message ?: "Network request failed."
            )
        } catch (error: Exception) {
            MONUServerResponse(
                success = false,
                error = error.message ?: "Server request failed."
            )
        }
    }

    fun checkHealth(): ConnectionStatus {
        val current = config()

        if (current.baseUrl.isBlank()) {
            return ConnectionStatus(
                apkToServer = ConnectionState.NOT_CONFIGURED,
                message = "MONU Server URL is not configured."
            )
        }

        val started = System.currentTimeMillis()
        val result =
            get(
                resolveUrl(
                    current.baseUrl,
                    current.healthPath
                )
            )

        val latency =
            System.currentTimeMillis() - started

        return if (result.success) {
            ConnectionStatus(
                apkToServer = ConnectionState.CONNECTED,
                serverToApk = ConnectionState.UNKNOWN,
                lastCheckedAt = System.currentTimeMillis(),
                latencyMs = latency,
                message = "Real MONU Server health request succeeded."
            )
        } else {
            ConnectionStatus(
                apkToServer = ConnectionState.DISCONNECTED,
                serverToApk = ConnectionState.UNKNOWN,
                lastCheckedAt = System.currentTimeMillis(),
                latencyMs = latency,
                message =
                    result.error ?: "MONU Server health request failed."
            )
        }
    }

    fun discoverCapabilities(): CapabilityStatus {
        val current = config()

        if (current.baseUrl.isBlank()) {
            return CapabilityStatus(
                success = false,
                rawResponse = "",
                error = "Server URL is not configured."
            )
        }

        val result =
            get(
                resolveUrl(
                    current.baseUrl,
                    current.capabilitiesPath
                )
            )

        return CapabilityStatus(
            success = result.success,
            rawResponse = result.body,
            error = result.error
        )
    }

    fun sendChat(
        message: String,
        conversationId: String? = null
    ): MONUServerResponse {
        val current = config()

        if (current.chatPath.isBlank()) {
            return MONUServerResponse(
                success = false,
                error = "Chat endpoint is not configured."
            )
        }

        val payload =
            JSONObject()
                .put("message", message)
                .put("query", message)
                .put("text", message)

        if (!conversationId.isNullOrBlank()) {
            payload.put("conversation_id", conversationId)
            payload.put("conversationId", conversationId)
        }

        return postJson(
            resolveUrl(
                current.baseUrl,
                current.chatPath
            ),
            payload
        )
    }

    fun sendCommand(
        command: String,
        commandId: String? = null
    ): MONUServerResponse {
        val current = config()

        if (current.commandPath.isBlank()) {
            return MONUServerResponse(
                success = false,
                error = "Command endpoint is not configured."
            )
        }

        val payload =
            JSONObject()
                .put("command", command)
                .put("message", command)
                .put("text", command)

        if (!commandId.isNullOrBlank()) {
            payload.put("command_id", commandId)
            payload.put("commandId", commandId)
        }

        return postJson(
            resolveUrl(
                current.baseUrl,
                current.commandPath
            ),
            payload
        )
    }

    private fun extractText(body: String): String {
        if (body.isBlank()) return ""

        return try {
            val root = JSONObject(body)

            val keys =
                listOf(
                    "text",
                    "response",
                    "reply",
                    "message",
                    "content",
                    "answer",
                    "output"
                )

            for (key in keys) {
                val value = root.opt(key)
                if (value is String && value.isNotBlank()) {
                    return value
                }
            }

            val nestedKeys =
                listOf(
                    "data",
                    "result",
                    "response"
                )

            for (key in nestedKeys) {
                val nested = root.opt(key)

                if (nested is JSONObject) {
                    for (nestedKey in keys) {
                        val value = nested.opt(nestedKey)
                        if (value is String && value.isNotBlank()) {
                            return value
                        }
                    }
                }
            }

            body
        } catch (_: Exception) {
            try {
                val array = JSONArray(body)
                if (array.length() > 0) {
                    array.optString(0, body)
                } else {
                    body
                }
            } catch (_: Exception) {
                body
            }
        }
    }
}
