#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 2...          "
echo "=========================================="

PACKAGE_DIR=$(find app/src/main/java -type d -name "repository" -o -name "data" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/repository"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUPersistentChatRepository.kt"
package PACKAGE_NAME_PLACEHOLDER

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class MONUChatMessage(
    val sender: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MONUPersistentChatRepository(context: Context) {

    private val chatFile = File(context.filesDir, "monu_chat_history.json")

    @Synchronized
    fun saveMessage(sender: String, text: String) {
        val history = getAllMessages().toMutableList()
        history.add(MONUChatMessage(sender, text))
        
        val jsonArray = JSONArray()
        history.forEach { msg ->
            val obj = JSONObject()
            obj.put("sender", msg.sender)
            obj.put("message", msg.message)
            obj.put("timestamp", msg.timestamp)
            jsonArray.put(obj)
        }
        
        chatFile.writeText(jsonArray.toString())
    }

    @Synchronized
    fun getAllMessages(): List<MONUChatMessage> {
        if (!chatFile.exists()) return emptyList()
        return try {
            val content = chatFile.readText()
            if (content.isBlank()) return emptyList()
            val jsonArray = JSONArray(content)
            val list = mutableListOf<MONUChatMessage>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    MONUChatMessage(
                        sender = obj.getString("sender"),
                        message = obj.getString("message"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    @Synchronized
    fun clearHistory() {
        if (chatFile.exists()) {
            chatFile.delete()
        }
    }
}
KOTLIN_EOF

sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUPersistentChatRepository.kt"

echo "[✓] Point 2 Resolved: Persistent Chat Storage Engine integrated."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
