package com.monu.mobile.core.network

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Multi-Source Internet Knowledge Fetcher (Fix for Point 10)
 * Expands knowledge retrieval beyond Wikipedia to include multi-engine search APIs.
 */
class MONUInternetKnowledgeEngine {

    suspend fun fetchKnowledge(query: String): String = withContext(Dispatchers.IO) {
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        
        val searchResult = fetchFromDuckDuckGo(encodedQuery)
        if (searchResult.isNotBlank()) {
            return@withContext searchResult
        }

        val wikiResult = fetchFromWikipedia(encodedQuery)
        if (wikiResult.isNotBlank()) {
            return@withContext wikiResult
        }

        return@withContext "No knowledge results found across internet sources for: $query"
    }

    private fun fetchFromDuckDuckGo(query: String): String {
        return try {
            val url = URL("https://api.duckduckgo.com/?q=$query&format=json&no_html=1&skip_disambig=1")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val abstractText = json.optString("AbstractText", "")
                if (abstractText.isNotBlank()) {
                    return "Source: DuckDuckGo | $abstractText"
                }
            }
            ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun fetchFromWikipedia(query: String): String {
        return try {
            val url = URL("https://en.wikipedia.org/api/rest_v1/page/summary/$query")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val extract = json.optString("extract", "")
                if (extract.isNotBlank()) {
                    return "Source: Wikipedia | $extract"
                }
            }
            ""
        } catch (e: Exception) {
            ""
        }
    }
}
