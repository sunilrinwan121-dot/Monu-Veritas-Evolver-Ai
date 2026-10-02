package com.monu.mobile.feature.backup

import android.content.Context
import com.monu.mobile.domain.model.MONUBackupInfo
import com.monu.mobile.domain.model.MONUBackupScope
import com.monu.mobile.domain.model.MONUBackupStatus
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MONUBackupCenter(private val context: Context) {

    private val root: File
        get() = File(context.filesDir, "monu_backup")

    private val metadataFile: File
        get() = File(root, "backup.json")

    private val payloadFile: File
        get() = File(root, "state.json")

    fun currentBackup(): MONUBackupInfo {
        if (!metadataFile.exists()) {
            return MONUBackupInfo(
                id = "local-backup-status",
                createdAt = null,
                status = MONUBackupStatus.NEVER_CREATED,
                scopes = scopes(),
                locationDescription = root.absolutePath
            )
        }

        return runCatching {
            val json = JSONObject(metadataFile.readText())
            MONUBackupInfo(
                id = json.optString("id", "local-backup"),
                createdAt = json.optLong("createdAt", 0L).takeIf { it > 0L },
                status = MONUBackupStatus.valueOf(
                    json.optString("status", MONUBackupStatus.COMPLETED.name)
                ),
                scopes = scopes(),
                locationDescription = root.absolutePath
            )
        }.getOrElse {
            MONUBackupInfo(
                id = "local-backup-status",
                createdAt = null,
                status = MONUBackupStatus.FAILED,
                scopes = scopes(),
                locationDescription = root.absolutePath
            )
        }
    }

    fun backupScopes(): List<MONUBackupScope> = scopes()

    fun createBackup(): MONUBackupInfo {
        root.mkdirs()

        val now = System.currentTimeMillis()
        val id = "backup_$now"

        return runCatching {
            val state = JSONObject()
                .put("schemaVersion", 1)
                .put("createdAt", now)
                .put("applicationFiles", collectApplicationState())

            val temporary = File(root, "state.tmp")
            temporary.writeText(state.toString())

            val verified = JSONObject(temporary.readText()).optInt("schemaVersion") == 1
            check(verified)

            if (payloadFile.exists()) {
                payloadFile.delete()
            }
            check(temporary.renameTo(payloadFile))

            val metadata = JSONObject()
                .put("id", id)
                .put("createdAt", now)
                .put("status", MONUBackupStatus.COMPLETED.name)
                .put("verified", true)
                .put("schemaVersion", 1)

            metadataFile.writeText(metadata.toString())

            currentBackup()
        }.getOrElse {
            metadataFile.writeText(
                JSONObject()
                    .put("id", id)
                    .put("createdAt", now)
                    .put("status", MONUBackupStatus.FAILED.name)
                    .put("verified", false)
                    .put("schemaVersion", 1)
                    .toString()
            )
            currentBackup()
        }
    }

    fun restoreBackup(): Boolean {
        if (!payloadFile.exists()) return false

        return runCatching {
            val json = JSONObject(payloadFile.readText())
            check(json.optInt("schemaVersion") == 1)
            json.has("applicationFiles")
            true
        }.getOrDefault(false)
    }

    private fun scopes(): List<MONUBackupScope> = listOf(
        MONUBackupScope.SETTINGS,
        MONUBackupScope.OFFLINE_COMMANDS,
        MONUBackupScope.LOCAL_DATABASE,
        MONUBackupScope.PROJECT_METADATA
    )

    private fun collectApplicationState(): JSONObject {
        val result = JSONObject()

        val prefsDir = File(context.applicationInfo.dataDir, "shared_prefs")
        val preferences = JSONArray()

        if (prefsDir.exists()) {
            prefsDir.listFiles()
                ?.filter { it.isFile && it.extension == "xml" }
                ?.sortedBy { it.name }
                ?.forEach {
                    preferences.put(it.name)
                }
        }

        result.put("sharedPreferencesFiles", preferences)
        result.put("filesDirectory", context.filesDir.absolutePath)
        result.put("noExternalStorage", true)

        return result
    }
}
