package com.monu.mobile.feature.knowledge

import com.monu.mobile.domain.model.InternetKnowledgeResult
import com.monu.mobile.domain.model.InternetKnowledgeState
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class MONUInternetKnowledgeEngine {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    fun masterCapabilities(): List<String> = listOf(
        "Internet knowledge retrieval",
        "Empty query validation",
        "UTF-8 query URL encoding",
        "Wikipedia REST API requests",
        "HTTP network execution",
        "Connection timeout handling",
        "Read timeout handling",
        "HTTP error detection",
        "JSON response parsing",
        "Knowledge title extraction",
        "Knowledge summary extraction",
        "Not-found state reporting",
        "Network error reporting",
        "Invalid query reporting"
    )

    fun masterPurpose(): String =
        "Retrieves factual internet knowledge summaries when local or cloud intelligence requires an external knowledge source."

    fun isAvailable(): Boolean = true

    fun search(query: String): InternetKnowledgeResult {

        val cleanedQuery = query.trim()

        if (cleanedQuery.isBlank()) {
            return InternetKnowledgeResult(
                query = query,
                title = "",
                summary = "",
                source = "Internet",
                state = InternetKnowledgeState.INVALID_QUERY,
                errorMessage = "Query is empty"
            )
        }

        return try {
            val encoded =
                URLEncoder.encode(cleanedQuery, "UTF-8")

            val url =
                "https://en.wikipedia.org/api/rest_v1/page/summary/$encoded"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "MONU-Mobile/1.0")
                .build()

            client.newCall(request).execute().use { response ->

                if (!response.isSuccessful) {
                    return InternetKnowledgeResult(
                        query = cleanedQuery,
                        title = "",
                        summary = "",
                        source = "Wikipedia",
                        state = InternetKnowledgeState.NOT_FOUND,
                        errorMessage =
                            "Internet source returned HTTP ${response.code}"
                    )
                }

                val body =
                    response.body?.string().orEmpty()

                val json = JSONObject(body)

                val title =
                    json.optString("title", cleanedQuery)

                val extract =
                    json.optString("extract", "")

                if (extract.isBlank()) {
                    InternetKnowledgeResult(
                        query = cleanedQuery,
                        title = title,
                        summary = "",
                        source = "Wikipedia",
                        state = InternetKnowledgeState.NOT_FOUND,
                        errorMessage =
                            "No useful knowledge summary found"
                    )
                } else {
                    InternetKnowledgeResult(
                        query = cleanedQuery,
                        title = title,
                        summary = extract,
                        source = "Wikipedia",
                        state = InternetKnowledgeState.SUCCESS
                    )
                }
            }

        } catch (error: Exception) {
            InternetKnowledgeResult(
                query = cleanedQuery,
                title = "",
                summary = "",
                source = "Internet",
                state = InternetKnowledgeState.NETWORK_ERROR,
                errorMessage =
                    when (error) {
                        is java.net.UnknownHostException ->
                            "Internet connection is unavailable."

                        is java.net.SocketTimeoutException ->
                            "The internet request timed out."

                        is java.io.IOException ->
                            "The internet request failed."

                        else ->
                            "The internet knowledge service could not complete the request."
                    }
            )
        }
    }
}
