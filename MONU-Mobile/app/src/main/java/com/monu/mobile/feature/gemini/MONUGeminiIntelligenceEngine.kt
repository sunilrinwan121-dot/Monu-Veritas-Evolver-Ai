package com.monu.mobile.feature.gemini

import com.monu.mobile.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class MONUModelTask {
    SIMPLE,
    GENERAL,
    REASONING,
    ADVANCED
}

data class MONUModelProfile(
    val name: String,
    val tasks: Set<MONUModelTask>,
    val priority: Int
)

data class MONUGeminiResult(
    val success: Boolean,
    val text: String = "",
    val model: String = "",
    val error: String = ""
)

class MONUGeminiIntelligenceEngine {

    private val apiKey: String
        get() = BuildConfig.GEMINI_API_KEY.trim()

    /*
     * Model selection policy is separated from HTTP execution.
     * If a model is unavailable, the next compatible model is tried.
     */
    private val modelProfiles = listOf(
        MONUModelProfile(
            name = "gemini-3.1-flash-lite",
            tasks = setOf(MONUModelTask.SIMPLE, MONUModelTask.GENERAL),
            priority = 1
        ),
        MONUModelProfile(
            name = "gemini-3.5-flash",
            tasks = setOf(MONUModelTask.GENERAL, MONUModelTask.REASONING),
            priority = 2
        ),
        MONUModelProfile(
            name = "gemini-3-flash-preview",
            tasks = setOf(MONUModelTask.GENERAL, MONUModelTask.REASONING),
            priority = 3
        ),
        MONUModelProfile(
            name = "gemini-2.5-flash",
            tasks = setOf(
                MONUModelTask.SIMPLE,
                MONUModelTask.GENERAL,
                MONUModelTask.REASONING
            ),
            priority = 4
        ),
        MONUModelProfile(
            name = "gemini-2.5-pro",
            tasks = setOf(
                MONUModelTask.REASONING,
                MONUModelTask.ADVANCED
            ),
            priority = 5
        ),
        MONUModelProfile(
            name = "gemini-3.1-pro-preview",
            tasks = setOf(MONUModelTask.ADVANCED),
            priority = 6
        )
    )

    fun masterCapabilities(): List<String> = listOf(
        "Cloud AI reasoning",
        "Gemini API authentication",
        "Query complexity classification",
        "Task-aware model selection",
        "Priority-based model routing",
        "Compatible model fallback",
        "HTTPS request execution",
        "JSON response parsing",
        "Candidate text extraction",
        "Timeout handling",
        "Structured success and error reporting"
    )

    fun modelProfiles(): List<MONUModelProfile> =
        modelProfiles.toList()

    fun isConfigured(): Boolean =
        apiKey.isNotBlank() &&
            apiKey != "PASTE_YOUR_GEMINI_API_KEY_HERE"

    fun classifyTask(prompt: String): MONUModelTask {
        val text = prompt.lowercase()

        return when {
            text.length > 1200 ||
                listOf(
                    "analyze deeply",
                    "complex reasoning",
                    "step by step reasoning",
                    "architecture",
                    "design system",
                    "compare multiple",
                    "research"
                ).any { text.contains(it) } ->
                MONUModelTask.ADVANCED

            listOf(
                "why",
                "how does",
                "explain",
                "compare",
                "solve",
                "debug",
                "plan"
            ).any { text.contains(it) } ->
                MONUModelTask.REASONING

            text.length <= 80 ->
                MONUModelTask.SIMPLE

            else ->
                MONUModelTask.GENERAL
        }
    }

    private fun selectModels(
        task: MONUModelTask
    ): List<MONUModelProfile> {
        val compatible =
            modelProfiles
                .filter { task in it.tasks }
                .sortedBy { it.priority }

        val fallback =
            modelProfiles
                .filter { it !in compatible }
                .sortedBy { it.priority }

        return compatible + fallback
    }

    suspend fun ask(
        prompt: String
    ): MONUGeminiResult = withContext(Dispatchers.IO) {

        val cleanPrompt = prompt.trim()

        if (cleanPrompt.isBlank()) {
            return@withContext MONUGeminiResult(
                success = false,
                error = "Empty prompt."
            )
        }

        if (!isConfigured()) {
            return@withContext MONUGeminiResult(
                success = false,
                error = "Gemini API key is not configured."
            )
        }

        val task = classifyTask(cleanPrompt)
        val models = selectModels(task)

        var lastError = "No Gemini model was available."

        for (profile in models) {
            val result = askModel(
                model = profile.name,
                prompt = cleanPrompt
            )

            if (result.success && result.text.isNotBlank()) {
                return@withContext result
            }

            if (result.error.isNotBlank()) {
                lastError = "${profile.name}: ${result.error}"
            }
        }

        MONUGeminiResult(
            success = false,
            error = lastError
        )
    }

    private fun askModel(
        model: String,
        prompt: String
    ): MONUGeminiResult {

        var connection: HttpURLConnection? = null

        return try {
            val encodedKey = URLEncoder.encode(
                apiKey,
                StandardCharsets.UTF_8.toString()
            )

            val endpoint =
                "https://generativelanguage.googleapis.com/" +
                    "v1beta/models/$model:generateContent?key=$encodedKey"

            connection =
                (URL(endpoint).openConnection()
                    as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 15000
                    readTimeout = 30000
                    doOutput = true
                    setRequestProperty(
                        "Content-Type",
                        "application/json; charset=utf-8"
                    )
                }

            val requestJson = JSONObject().apply {
                put(
                    "contents",
                    JSONArray().put(
                        JSONObject().apply {
                            put(
                                "parts",
                                JSONArray().put(
                                    JSONObject().put(
                                        "text",
                                        prompt
                                    )
                                )
                            )
                        }
                    )
                )
            }

            connection.outputStream.use { output ->
                output.write(
                    requestJson.toString()
                        .toByteArray(StandardCharsets.UTF_8)
                )
            }

            val code = connection.responseCode

            val stream =
                if (code in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

            val body =
                stream?.use {
                    BufferedReader(
                        InputStreamReader(
                            it,
                            StandardCharsets.UTF_8
                        )
                    ).readText()
                }.orEmpty()

            if (code !in 200..299) {
                return MONUGeminiResult(
                    success = false,
                    model = model,
                    error = "HTTP $code"
                )
            }

            val candidates =
                JSONObject(body).optJSONArray("candidates")

            val parts =
                candidates
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")

            val text = buildString {
                if (parts != null) {
                    for (i in 0 until parts.length()) {
                        val value =
                            parts.optJSONObject(i)
                                ?.optString("text")
                                ?.trim()
                                .orEmpty()

                        if (value.isNotBlank()) {
                            if (isNotEmpty()) append("\n")
                            append(value)
                        }
                    }
                }
            }.trim()

            if (text.isBlank()) {
                MONUGeminiResult(
                    success = false,
                    model = model,
                    error = "Gemini returned no usable text."
                )
            } else {
                MONUGeminiResult(
                    success = true,
                    text = text,
                    model = model
                )
            }

        } catch (error: Exception) {
            MONUGeminiResult(
                success = false,
                model = model,
                error =
                    when (error) {
                        is java.net.UnknownHostException ->
                            "Gemini service could not be reached."

                        is java.net.SocketTimeoutException ->
                            "Gemini request timed out."

                        is java.io.IOException ->
                            "Gemini network request failed."

                        else ->
                            "Gemini could not complete the request."
                    }
            )
        } finally {
            connection?.disconnect()
        }
    }
}
