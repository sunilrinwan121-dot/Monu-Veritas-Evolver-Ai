#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fixes for Point 10 & 11...   "
echo "=========================================="

PACKAGE_DIR=$(find app/src/main/java -type d -name "network" -o -name "data" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/network"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUInternetKnowledgeEngine.kt"
package PACKAGE_NAME_PLACEHOLDER

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
KOTLIN_EOF

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUServerIntegrationHub.kt"
package PACKAGE_NAME_PLACEHOLDER

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
KOTLIN_EOF

sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUInternetKnowledgeEngine.kt"
sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUServerIntegrationHub.kt"

echo "[✓] Point 10 Resolved: Internet Knowledge expanded with DuckDuckGo API + multi-source fallback."
echo "[✓] Point 11 Resolved: MONUServerIntegrationHub created for real REST API & Backend sync."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
