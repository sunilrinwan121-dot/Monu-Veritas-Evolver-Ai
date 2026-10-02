package com.monu.mobile.feature.memory

import android.content.Context
import com.monu.mobile.data.security.MONUSecureMemoryStore
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class MONUMemory(
    val id: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val accessCount: Int = 0
)

class MONUMemoryEngine(context: Context) {

    companion object {
        private const val MAX_MEMORIES = 200
        private const val MAX_MEMORY_CHARS = 2000
        private const val MAX_TOTAL_CHARS = 120000
        private const val DEFAULT_RECALL_LIMIT = 8
        private const val DEFAULT_RECENT_LIMIT = 20
    }

    private val secureStore =
        MONUSecureMemoryStore(
            context.applicationContext
        )

    @Synchronized
    fun remember(content: String): MONUMemory? {

        val cleanContent =
            content
                .trim()
                .replace(
                    Regex("\\s+"),
                    " "
                )
                .take(MAX_MEMORY_CHARS)

        if (cleanContent.isBlank()) {
            return null
        }

        val memories =
            loadAll().toMutableList()

        val existingIndex =
            memories.indexOfFirst {
                it.content.equals(
                    cleanContent,
                    ignoreCase = true
                )
            }

        val now =
            System.currentTimeMillis()

        if (existingIndex >= 0) {

            val existing =
                memories[existingIndex]

            val updated =
                existing.copy(
                    updatedAt = now,
                    accessCount =
                        existing.accessCount + 1
                )

            memories[existingIndex] =
                updated

            saveAll(memories)

            return updated
        }

        val memory =
            MONUMemory(
                id =
                    UUID.randomUUID()
                        .toString(),
                content = cleanContent,
                createdAt = now,
                updatedAt = now,
                accessCount = 0
            )

        memories.add(memory)

        val bounded =
            enforceLimits(memories)

        saveAll(bounded)

        return if (
            bounded.any {
                it.id == memory.id
            }
        ) {
            memory
        } else {
            null
        }
    }

    @Synchronized
    fun recall(
        query: String,
        limit: Int = DEFAULT_RECALL_LIMIT
    ): List<MONUMemory> {

        val safeLimit =
            limit.coerceIn(1, DEFAULT_RECALL_LIMIT)

        val cleanQuery =
            query.trim().lowercase()

        if (cleanQuery.isBlank()) {
            return recent(safeLimit)
        }

        val queryWords =
            tokenize(cleanQuery)

        if (queryWords.isEmpty()) {
            return recent(safeLimit)
        }

        val ranked =
            loadAll()
                .map { memory ->

                    val memoryWords =
                        tokenize(
                            memory.content.lowercase()
                        )

                    val matchedWords =
                        queryWords.count {
                            it in memoryWords
                        }

                    val exactBonus =
                        if (
                            memory.content
                                .lowercase()
                                .contains(cleanQuery)
                        ) {
                            10
                        } else {
                            0
                        }

                    val frequencyBonus =
                        memory.accessCount
                            .coerceAtMost(5)

                    val score =
                        matchedWords * 5 +
                            exactBonus +
                            frequencyBonus

                    memory to score
                }
                .filter {
                    it.second > 0
                }
                .sortedWith(
                    compareByDescending<
                        Pair<MONUMemory, Int>
                    > {
                        it.second
                    }.thenByDescending {
                        it.first.updatedAt
                    }
                )
                .take(safeLimit)
                .map {
                    it.first
                }

        if (ranked.isNotEmpty()) {
            ranked.forEach {
                touch(it.id)
            }
        }

        return ranked
    }

    @Synchronized
    fun recent(
        limit: Int = DEFAULT_RECENT_LIMIT
    ): List<MONUMemory> {

        val safeLimit =
            limit.coerceIn(
                1,
                MAX_MEMORIES
            )

        return loadAll()
            .sortedByDescending {
                it.updatedAt
            }
            .take(safeLimit)
    }

    @Synchronized
    fun all(): List<MONUMemory> {
        return loadAll()
            .sortedByDescending {
                it.updatedAt
            }
    }

    @Synchronized
    fun contextFor(
        query: String,
        limit: Int = 6,
        maxCharacters: Int = 4000
    ): String {

        val relevant =
            recall(
                query,
                limit.coerceIn(
                    1,
                    DEFAULT_RECALL_LIMIT
                )
            )

        if (relevant.isEmpty()) {
            return ""
        }

        val safeMax =
            maxCharacters.coerceIn(
                256,
                16000
            )

        val builder =
            StringBuilder()

        relevant.forEachIndexed {
                index,
                memory ->

            val line =
                "${index + 1}. " +
                    "${memory.content}\n"

            if (
                builder.length +
                    line.length <=
                    safeMax
            ) {
                builder.append(line)
            }
        }

        return builder
            .toString()
            .trim()
    }

    @Synchronized
    fun forget(memoryId: String): Boolean {

        val memories =
            loadAll().toMutableList()

        val removed =
            memories.removeAll {
                it.id == memoryId
            }

        if (removed) {
            saveAll(memories)
        }

        return removed
    }

    @Synchronized
    fun forgetMatching(query: String): Int {

        val clean =
            query.trim().lowercase()

        if (clean.isBlank()) {
            return 0
        }

        val memories =
            loadAll().toMutableList()

        val before =
            memories.size

        memories.removeAll {
            it.content
                .lowercase()
                .contains(clean)
        }

        val removed =
            before - memories.size

        if (removed > 0) {
            saveAll(memories)
        }

        return removed
    }

    @Synchronized
    fun clearAll() {
        secureStore.clear()
    }

    fun memoryCount(): Int {
        return loadAll().size
    }

    private fun touch(memoryId: String) {

        val memories =
            loadAll().toMutableList()

        val index =
            memories.indexOfFirst {
                it.id == memoryId
            }

        if (index >= 0) {

            val old =
                memories[index]

            memories[index] =
                old.copy(
                    updatedAt =
                        System.currentTimeMillis(),
                    accessCount =
                        old.accessCount + 1
                )

            saveAll(memories)
        }
    }

    private fun tokenize(
        text: String
    ): Set<String> {

        return text
            .lowercase()
            .split(
                Regex(
                    "[^\\p{L}\\p{N}]+"
                )
            )
            .map {
                it.trim()
            }
            .filter {
                it.length >= 2
            }
            .toSet()
    }

    private fun loadAll():
        List<MONUMemory> {

        val raw =
            secureStore.read()
                ?: "[]"

        return try {

            val array =
                JSONArray(raw)

            buildList {

                for (
                    index
                    in 0 until array.length()
                ) {

                    val json =
                        array.optJSONObject(
                            index
                        ) ?: continue

                    val content =
                        json
                            .optString(
                                "content",
                                ""
                            )
                            .trim()
                            .take(
                                MAX_MEMORY_CHARS
                            )

                    if (content.isBlank()) {
                        continue
                    }

                    add(
                        MONUMemory(
                            id =
                                json.optString(
                                    "id",
                                    UUID.randomUUID()
                                        .toString()
                                ),
                            content = content,
                            createdAt =
                                json.optLong(
                                    "createdAt",
                                    System.currentTimeMillis()
                                ),
                            updatedAt =
                                json.optLong(
                                    "updatedAt",
                                    System.currentTimeMillis()
                                ),
                            accessCount =
                                json.optInt(
                                    "accessCount",
                                    0
                                )
                        )
                    )
                }
            }
                .let {
                    enforceLimits(it)
                }

        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun saveAll(
        memories: List<MONUMemory>
    ) {

        val bounded =
            enforceLimits(memories)

        val array =
            JSONArray()

        bounded.forEach { memory ->

            array.put(
                JSONObject().apply {

                    put(
                        "id",
                        memory.id
                    )

                    put(
                        "content",
                        memory.content
                            .take(
                                MAX_MEMORY_CHARS
                            )
                    )

                    put(
                        "createdAt",
                        memory.createdAt
                    )

                    put(
                        "updatedAt",
                        memory.updatedAt
                    )

                    put(
                        "accessCount",
                        memory.accessCount
                    )
                }
            )
        }

        secureStore.write(
            array.toString()
        )
    }

    private fun enforceLimits(
        input: List<MONUMemory>
    ): List<MONUMemory> {

        val normalized =
            input
                .asSequence()
                .filter {
                    it.content.isNotBlank()
                }
                .map {
                    it.copy(
                        content =
                            it.content
                                .trim()
                                .replace(
                                    Regex("\\s+"),
                                    " "
                                )
                                .take(
                                    MAX_MEMORY_CHARS
                                )
                    )
                }
                .sortedByDescending {
                    it.updatedAt
                }
                .take(MAX_MEMORIES)
                .toList()

        var totalChars = 0

        return normalized
            .filter { memory ->

                val next =
                    totalChars +
                        memory.content.length

                if (
                    next <=
                    MAX_TOTAL_CHARS
                ) {
                    totalChars = next
                    true
                } else {
                    false
                }
            }
    }
}
