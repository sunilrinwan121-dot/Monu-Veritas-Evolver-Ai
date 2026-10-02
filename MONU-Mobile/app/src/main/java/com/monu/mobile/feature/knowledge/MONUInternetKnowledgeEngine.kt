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
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .callTimeout(25, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    fun masterCapabilities(): List<String> = listOf(
        "Multi-source internet knowledge retrieval",
        "Wikipedia MediaWiki search",
        "Wikipedia article extraction",
        "Wikipedia REST summary fallback",
        "Hindi Wikipedia support",
        "English Wikipedia support",
        "DuckDuckGo Instant Answer fallback",
        "UTF-8 query encoding",
        "HTTPS-only external sources",
        "Connection timeout handling",
        "Read timeout handling",
        "Call timeout handling",
        "Automatic connection retry",
        "HTTP error detection",
        "JSON validation",
        "Source attribution",
        "Source URL reporting",
        "Empty query validation",
        "Network error reporting",
        "Not-found reporting"
    )

    fun masterPurpose(): String =
        "Retrieves factual internet knowledge through multiple public HTTPS knowledge sources with deterministic fallback and source attribution."

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

        val failures = mutableListOf<String>()

        val primaryLanguage = detectWikipediaLanguage(cleanedQuery)

        val primaryWikipedia = tryWikipediaSearch(
            cleanedQuery,
            primaryLanguage,
            failures
        )

        if (primaryWikipedia != null) {
            return primaryWikipedia
        }

        if (primaryLanguage != "en") {
            val englishWikipedia = tryWikipediaSearch(
                cleanedQuery,
                "en",
                failures
            )

            if (englishWikipedia != null) {
                return englishWikipedia
            }
        }

        val duckDuckGo = tryDuckDuckGo(cleanedQuery, failures)

        if (duckDuckGo != null) {
            return duckDuckGo
        }

        val restFallback = tryWikipediaRest(
            cleanedQuery,
            primaryLanguage,
            failures
        )

        if (restFallback != null) {
            return restFallback
        }

        val message =
            failures
                .filter { it.isNotBlank() }
                .take(4)
                .joinToString("; ")

        return InternetKnowledgeResult(
            query = cleanedQuery,
            title = "",
            summary = "",
            source = "Internet",
            state =
                if (failures.any {
                    it.contains("HTTP") ||
                    it.contains("timeout", ignoreCase = true) ||
                    it.contains("network", ignoreCase = true)
                }) {
                    InternetKnowledgeState.NETWORK_ERROR
                } else {
                    InternetKnowledgeState.NOT_FOUND
                },
            errorMessage =
                if (message.isBlank()) {
                    "No useful internet result was found."
                } else {
                    message
                }
        )
    }

    private fun detectWikipediaLanguage(query: String): String {
        return if (query.any { it in '\u0900'..'\u097F' }) {
            "hi"
        } else {
            "en"
        }
    }

    private fun tryWikipediaSearch(
        query: String,
        language: String,
        failures: MutableList<String>
    ): InternetKnowledgeResult? {

        return try {
            val encodedQuery =
                URLEncoder.encode(query, "UTF-8")

            val apiUrl =
                "https://$language.wikipedia.org/w/api.php" +
                    "?action=query" +
                    "&list=search" +
                    "&srsearch=$encodedQuery" +
                    "&srlimit=5" +
                    "&format=json" +
                    "&utf8=1"

            val searchJson =
                getJson(apiUrl, "Wikipedia-$language", failures)
                    ?: return null

            val searchArray =
                searchJson
                    .optJSONObject("query")
                    ?.optJSONArray("search")
                    ?: return null

            if (searchArray.length() == 0) {
                return null
            }

            for (index in 0 until searchArray.length()) {
                val item = searchArray.optJSONObject(index) ?: continue
                val pageId = item.optLong("pageid", 0L)

                if (pageId <= 0L) {
                    continue
                }

                val title =
                    item.optString("title", query)

                val page =
                    fetchWikipediaExtract(
                        language = language,
                        pageId = pageId,
                        fallbackTitle = title,
                        failures = failures
                    )

                if (page != null && page.summary.isNotBlank()) {
                    return page
                }
            }

            null
        } catch (error: Exception) {
            failures +=
                "Wikipedia-$language search error: ${error.message ?: "unknown error"}"
            null
        }
    }

    private fun fetchWikipediaExtract(
        language: String,
        pageId: Long,
        fallbackTitle: String,
        failures: MutableList<String>
    ): InternetKnowledgeResult? {

        return try {
            val apiUrl =
                "https://$language.wikipedia.org/w/api.php" +
                    "?action=query" +
                    "&prop=extracts%7Cinfo" +
                    "&exintro=1" +
                    "&explaintext=1" +
                    "&exchars=6000" +
                    "&inprop=url" +
                    "&pageids=$pageId" +
                    "&format=json" +
                    "&utf8=1"

            val json =
                getJson(apiUrl, "Wikipedia-$language", failures)
                    ?: return null

            val pages =
                json
                    .optJSONObject("query")
                    ?.optJSONObject("pages")
                    ?: return null

            val page =
                pages.optJSONObject(pageId.toString())
                    ?: pages.keys().asSequence().mapNotNull {
                        pages.optJSONObject(it)
                    }.firstOrNull()
                    ?: return null

            val title =
                page.optString("title", fallbackTitle)

            val extract =
                page.optString("extract", "").trim()

            val sourceUrl =
                page.optString(
                    "fullurl",
                    "https://$language.wikipedia.org/wiki/" +
                        URLEncoder.encode(
                            title.replace(" ", "_"),
                            "UTF-8"
                        )
                )

            if (extract.isBlank()) {
                return null
            }

            InternetKnowledgeResult(
                query = fallbackTitle,
                title = title,
                summary = extract,
                source = "Wikipedia ($language)",
                state = InternetKnowledgeState.SUCCESS,
                sourceUrl = sourceUrl
            )
        } catch (error: Exception) {
            failures +=
                "Wikipedia-$language extract error: ${error.message ?: "unknown error"}"
            null
        }
    }

    private fun tryWikipediaRest(
        query: String,
        language: String,
        failures: MutableList<String>
    ): InternetKnowledgeResult? {

        return try {
            val encoded =
                URLEncoder.encode(query, "UTF-8")

            val url =
                "https://$language.wikipedia.org/api/rest_v1/page/summary/$encoded"

            val json =
                getJson(url, "Wikipedia REST", failures)
                    ?: return null

            val title =
                json.optString("title", query)

            val extract =
                json.optString("extract", "").trim()

            if (extract.isBlank()) {
                return null
            }

            val sourceUrl =
                json.optJSONObject("content_urls")
                    ?.optJSONObject("desktop")
                    ?.optString("page", "")
                    .orEmpty()

            InternetKnowledgeResult(
                query = query,
                title = title,
                summary = extract,
                source = "Wikipedia REST ($language)",
                state = InternetKnowledgeState.SUCCESS,
                sourceUrl =
                    sourceUrl.ifBlank {
                        "https://$language.wikipedia.org/wiki/" +
                            URLEncoder.encode(
                                title.replace(" ", "_"),
                                "UTF-8"
                            )
                    }
            )
        } catch (error: Exception) {
            failures +=
                "Wikipedia REST error: ${error.message ?: "unknown error"}"
            null
        }
    }

    private fun tryDuckDuckGo(
        query: String,
        failures: MutableList<String>
    ): InternetKnowledgeResult? {

        return try {
            val encoded =
                URLEncoder.encode(query, "UTF-8")

            val url =
                "https://api.duckduckgo.com/" +
                    "?q=$encoded" +
                    "&format=json" +
                    "&no_html=1" +
                    "&skip_disambig=0"

            val json =
                getJson(url, "DuckDuckGo", failures)
                    ?: return null

            val heading =
                json.optString("Heading", query).trim()

            val abstractText =
                json.optString("AbstractText", "").trim()

            val abstractUrl =
                json.optString("AbstractURL", "").trim()

            if (abstractText.isNotBlank()) {
                return InternetKnowledgeResult(
                    query = query,
                    title =
                        heading.ifBlank {
                            query
                        },
                    summary = abstractText,
                    source = "DuckDuckGo Instant Answer",
                    state = InternetKnowledgeState.SUCCESS,
                    sourceUrl = abstractUrl
                )
            }

            val relatedTopics =
                json.optJSONArray("RelatedTopics")

            if (relatedTopics != null) {
                for (index in 0 until relatedTopics.length()) {
                    val item =
                        relatedTopics.optJSONObject(index)
                            ?: continue

                    val text =
                        item.optString("Text", "").trim()

                    val firstUrl =
                        item.optString("FirstURL", "").trim()

                    if (text.isNotBlank()) {
                        return InternetKnowledgeResult(
                            query = query,
                            title = heading.ifBlank { query },
                            summary = text,
                            source = "DuckDuckGo Related Results",
                            state = InternetKnowledgeState.SUCCESS,
                            sourceUrl = firstUrl
                        )
                    }
                }
            }

            null
        } catch (error: Exception) {
            failures +=
                "DuckDuckGo error: ${error.message ?: "unknown error"}"
            null
        }
    }

    private fun getJson(
        url: String,
        sourceName: String,
        failures: MutableList<String>
    ): JSONObject? {

        return try {
            val request =
                Request.Builder()
                    .url(url)
                    .header(
                        "User-Agent",
                        "MONU-Mobile/1.1 (InternetKnowledgeEngine)"
                    )
                    .header(
                        "Accept",
                        "application/json"
                    )
                    .get()
                    .build()

            client.newCall(request).execute().use { response ->

                if (!response.isSuccessful) {
                    failures +=
                        "$sourceName HTTP ${response.code}"
                    return null
                }

                val body =
                    response.body?.string().orEmpty()

                if (body.isBlank()) {
                    failures +=
                        "$sourceName returned an empty response"
                    return null
                }

                JSONObject(body)
            }
        } catch (error: Exception) {
            failures +=
                "$sourceName network error: ${error.message ?: "unknown error"}"
            null
        }
    }
}
