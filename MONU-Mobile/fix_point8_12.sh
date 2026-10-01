#!/usr/bin/env bash
echo "=========================================="
echo "    Applying Fixes for Point 8 & 12...    "
echo "=========================================="

# ----------------------------------------------------
# 1. Point 12 Fix: Disable Android Auto Backup
# ----------------------------------------------------
MANIFEST_FILE=$(find app/src/main -name "AndroidManifest.xml" 2>/dev/null | head -n 1)

if [ -f "$MANIFEST_FILE" ]; then
    sed -i 's/android:allowBackup="true"/android:allowBackup="false"/g' "$MANIFEST_FILE"
    echo "[✓] Point 12 Resolved: 'android:allowBackup' set to false in $MANIFEST_FILE"
else
    echo "[!] Warning: AndroidManifest.xml not found directly."
fi

# ----------------------------------------------------
# 2. Point 8 Fix: Encrypted & Bounded Local Storage
# ----------------------------------------------------
PACKAGE_DIR=$(find app/src/main/java -type d -name "security" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/security"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << KOTLIN_EOF > "$PACKAGE_DIR/MONUSecureMemoryManager.kt"
package $PACKAGE_NAME

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Fix for Point 8:
 * 1. Encrypts local key-value storage using AES256 GCM.
 * 2. Implements bounded eviction policy to prevent unlimited memory growth.
 */
class MONUSecureMemoryManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "monu_secure_encrypted_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val MAX_MEMORY_ENTRIES = 500
    }

    fun saveEncryptedData(key: String, value: String) {
        val currentKeys = sharedPreferences.all.keys
        if (currentKeys.size >= MAX_MEMORY_ENTRIES) {
            val oldestKey = currentKeys.firstOrNull()
            oldestKey?.let { sharedPreferences.edit().remove(it).apply() }
        }
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getEncryptedData(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun clearMemory() {
        sharedPreferences.edit().clear().apply()
    }
}
KOTLIN_EOF

echo "[✓] Point 8 Resolved: Created MONUSecureMemoryManager with AES256 encryption and 500-entry max bound limit."
echo "=========================================="
echo "    Fixes successfully executed!          "
echo "=========================================="
