package com.monu.mobile.core.network

object ServerConfig {

    /*
     * IMPORTANT:
     *
     * Replace this later with your real MONU Server URL.
     *
     * Examples:
     * http://192.168.1.100:8000
     * https://your-server-domain.com
     */

    const val BASE_URL = ""

    const val HEALTH_ENDPOINT = "/health"
    const val CAPABILITIES_ENDPOINT = "/capabilities"

    fun isConfigured(): Boolean {
        return BASE_URL.isNotBlank()
    }

    fun healthUrl(): String {
        return BASE_URL.trimEnd('/') + HEALTH_ENDPOINT
    }

    fun capabilitiesUrl(): String {
        return BASE_URL.trimEnd('/') + CAPABILITIES_ENDPOINT
    }
}
