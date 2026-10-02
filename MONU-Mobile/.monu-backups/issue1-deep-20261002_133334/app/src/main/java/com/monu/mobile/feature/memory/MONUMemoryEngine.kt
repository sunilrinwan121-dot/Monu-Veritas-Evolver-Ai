package com.monu.mobile.feature.memory

import android.content.Context
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

    private val appContext = context.applicationContext

    private val preferences =
        appContext.getSharedPreferences(
            "monu_persistent_memory",
            Context.MODE_PRIVATE
        )

    private val memoryKey = "memories_v1"

    @Synchronized
    fun remember(content: String): MONUMemory? {

        val cleanContent = content
            .trim()
            .replace(Regex("\\s+"), " ")

        if (cleanContent.isBlank()) {
            return null
        }

        val memories = loadAll().toMutableList()

        val existingIndex =
            memories.indexOfFirst {
                it.content.equals(
                    cleanContent,
                    ignoreCase = true
                )
            }

        val now = System.currentTimeMillis()

        if (existingIndex >= 0) {

            val existing = memories[existingIndex]

            val updated =
                existing.copy(
                    updatedAt = now,
                    accessCount = existing.accessCount + 1
                )

            memories[existingIndex] = updated

            saveAll(memories)

            return updated
        }

        val memory =
            MONUMemory(
                id = UUID.randomUUID().toString(),
                content = cleanContent,
                createdAt = now,
                updatedAt = now,
                accessCount = 0
            )

        memories.add(memory)

        saveAll(memories)

        return memory
    }

    @Synchronized
    fun recall(query: String, limit: Int = 8): List<MONUMemory> {

        val cleanQuery =
            query.trim()
                .lowercase()

        if (cleanQuery.isBlank()) {
            return recent(limit)
        }

        val queryWords =
            tokenize(cleanQuery)

        if (queryWords.isEmpty()) {
            return recent(limit)
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
                        memory.accessCount.coerceAtMost(5)

                    val score =
                        matchedWords * 5 +
                        exactBonus +
                        frequencyBonus

                    memory to score
                }
                .filter { it.second > 0 }
                .sortedWith(
                    compareByDescending<Pair<MONUMemory, Int>> {
                        it.second
                    }.thenByDescending {
                        it.first.updatedAt
                    }
                )
                .take(limit)
                .map { it.first }

        if (ranked.isNotEmpty()) {

            ranked.forEach { memory ->
                touch(memory.id)
            }
        }

        return ranked
    }

    @Synchronized
    fun recent(limit: Int = 20): List<MONUMemory> {
        return loadAll()
            .sortedByDescending {
                it.updatedAt
            }
            .take(limit)
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
            recall(query, limit)

        if (relevant.isEmpty()) {
            return ""
        }

        val builder = StringBuilder()

        relevant.forEachIndexed { index, memory ->

            val line =
                "${index + 1}. ${memory.content}\n"

            if (
                builder.length + line.length <=
                maxCharacters
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
            query.trim()
                .lowercase()

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
        preferences.edit()
            .remove(memoryKey)
            .apply()
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

    private fun tokenize(text: String): Set<String> {

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

    private fun loadAll(): List<MONUMemory> {

        val raw =
            preferences.getString(
                memoryKey,
                "[]"
            ) ?: "[]"

        return try {

            val array =
                JSONArray(raw)

            buildList {

                for (
                    index in 0 until array.length()
                ) {

                    val json =
                        array.optJSONObject(index)
                            ?: continue

                    val content =
                        json.optString("content", "")
                            .trim()

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

        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun saveAll(
        memories: List<MONUMemory>
    ) {

        val array =
            JSONArray()

        memories.forEach { memory ->

            array.put(
                JSONObject().apply {

                    put(
                        "id",
                        memory.id
                    )

                    put(
                        "content",
                        memory.content
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

        preferences.edit()
            .putString(
                memoryKey,
                array.toString()
            )
            .apply()
    }
}
