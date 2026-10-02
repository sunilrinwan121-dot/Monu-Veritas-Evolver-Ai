package com.monu.mobile.feature.security

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
