package com.monu.mobile.data.network

import com.monu.mobile.core.network.ServerConfig
import com.monu.mobile.domain.model.CapabilityStatus
import com.monu.mobile.domain.model.ConnectionState
import com.monu.mobile.domain.model.ConnectionStatus
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class MONUServerClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    fun checkHealth(): ConnectionStatus {

        if (!ServerConfig.isConfigured()) {
            return ConnectionStatus(
                apkToServer = ConnectionState.NOT_CONFIGURED,
                message = "MONU Server URL is not configured"
            )
        }

        val startTime = System.currentTimeMillis()

        return try {

            val request = Request.Builder()
                .url(ServerConfig.healthUrl())
                .get()
                .build()

            client.newCall(request).execute().use { response ->

                val latency =
                    System.currentTimeMillis() - startTime

                if (response.isSuccessful) {
                    ConnectionStatus(
                        apkToServer = ConnectionState.CONNECTED,
                        serverToApk = ConnectionState.UNKNOWN,
                        lastCheckedAt = System.currentTimeMillis(),
                        latencyMs = latency,
                        message = "Real server health request succeeded"
                    )
                } else {
                    ConnectionStatus(
                        apkToServer = ConnectionState.DISCONNECTED,
                        serverToApk = ConnectionState.UNKNOWN,
                        lastCheckedAt = System.currentTimeMillis(),
                        latencyMs = latency,
                        message = "Server returned HTTP ${response.code}"
                    )
                }
            }

        } catch (error: IOException) {

            ConnectionStatus(
                apkToServer = ConnectionState.DISCONNECTED,
                serverToApk = ConnectionState.UNKNOWN,
                lastCheckedAt = System.currentTimeMillis(),
                message = error.message ?: "Network connection failed"
            )

        } catch (error: Exception) {

            ConnectionStatus(
                apkToServer = ConnectionState.DISCONNECTED,
                serverToApk = ConnectionState.UNKNOWN,
                lastCheckedAt = System.currentTimeMillis(),
                message = error.message ?: "Unknown connection error"
            )
        }
    }

    fun discoverCapabilities(): CapabilityStatus {

        if (!ServerConfig.isConfigured()) {
            return CapabilityStatus(
                success = false,
                rawResponse = "",
                error = "Server URL is not configured"
            )
        }

        return try {

            val request = Request.Builder()
                .url(ServerConfig.capabilitiesUrl())
                .get()
                .build()

            client.newCall(request).execute().use { response ->

                val body =
                    response.body?.string().orEmpty()

                CapabilityStatus(
                    success = response.isSuccessful,
                    rawResponse = body,
                    error = if (response.isSuccessful) {
                        null
                    } else {
                        "HTTP ${response.code}"
                    }
                )
            }

        } catch (error: Exception) {

            CapabilityStatus(
                success = false,
                rawResponse = "",
                error = error.message
                    ?: "Capability discovery failed"
            )
        }
    }
}
