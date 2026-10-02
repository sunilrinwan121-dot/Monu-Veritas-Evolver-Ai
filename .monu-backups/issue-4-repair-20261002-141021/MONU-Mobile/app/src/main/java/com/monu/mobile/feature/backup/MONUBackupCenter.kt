
package com.monu.mobile.feature.backup

import android.content.Context
import com.monu.mobile.BuildConfig
import com.monu.mobile.data.local.MONUDatabaseProvider
import com.monu.mobile.domain.model.MONUBackupInfo
import com.monu.mobile.domain.model.MONUBackupScope
import com.monu.mobile.domain.model.MONUBackupStatus
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class MONUBackupCenter(
    context: Context
) {
    private val appContext = context.applicationContext
    private val backupRoot = File(appContext.filesDir, "monu_backups")

    private val scopes = listOf(
        MONUBackupScope.SETTINGS,
        MONUBackupScope.OFFLINE_COMMANDS,
        MONUBackupScope.LOCAL_DATABASE,
        MONUBackupScope.PROJECT_METADATA,
        MONUBackupScope.WORKFLOWS
    )

    init {
        backupRoot.mkdirs()
    }

    fun currentBackup(): MONUBackupInfo {
        val latest = listBackupFiles().maxByOrNull { it.lastModified() }

        if (latest == null) {
            return MONUBackupInfo(
                id = "none",
                createdAt = null,
                status = MONUBackupStatus.NEVER_CREATED,
                scopes = scopes,
                locationDescription = "No backup has been created.",
                filePath = null
            )
        }

        val checksum = runCatching { sha256(latest) }.getOrNull()

        return MONUBackupInfo(
            id = latest.nameWithoutExtension,
            createdAt = latest.lastModified(),
            status = MONUBackupStatus.COMPLETED,
            scopes = scopes,
            locationDescription = latest.absolutePath,
            filePath = latest.absolutePath,
            checksum = checksum,
            sizeBytes = latest.length()
        )
    }

    fun backupScopes(): List<MONUBackupScope> = scopes

    @Synchronized
    fun createBackup(): MONUBackupInfo {
        backupRoot.mkdirs()

        val id = "monu-backup-${System.currentTimeMillis()}-${UUID.randomUUID()}"
        val target = File(backupRoot, "$id.zip")
        val temp = File(backupRoot, "$id.tmp")

        return try {
            MONUDatabaseProvider.close()

            ZipOutputStream(
                BufferedOutputStream(FileOutputStream(temp))
            ).use { zip ->
                addDirectory(zip, File(appContext.dataDir, "shared_prefs"), "shared_prefs")
                addDirectory(zip, File(appContext.dataDir, "databases"), "databases")

                val metadata = JSONObject()
                    .put("format", "MONU_BACKUP_V1")
                    .put("applicationId", BuildConfig.APPLICATION_ID)
                    .put("versionCode", BuildConfig.VERSION_CODE)
                    .put("versionName", BuildConfig.VERSION_NAME)
                    .put("createdAt", System.currentTimeMillis())
                    .put("database", "monu_mobile.db")
                    .put("sharedPreferences", true)

                addBytes(
                    zip,
                    "metadata.json",
                    metadata.toString(2).toByteArray(Charsets.UTF_8)
                )

                addBytes(
                    zip,
                    "project_metadata.json",
                    JSONObject()
                        .put("status", "LOCAL_PROJECT_METADATA_SCOPE")
                        .put("note", "Only locally persisted APK data is included.")
                        .toString(2)
                        .toByteArray(Charsets.UTF_8)
                )
            }

            verifyZip(temp)
            if (target.exists()) target.delete()
            check(temp.renameTo(target)) { "Unable to finalize backup file." }

            MONUBackupInfo(
                id = id,
                createdAt = target.lastModified(),
                status = MONUBackupStatus.COMPLETED,
                scopes = scopes,
                locationDescription = target.absolutePath,
                filePath = target.absolutePath,
                checksum = sha256(target),
                sizeBytes = target.length()
            )
        } catch (error: Throwable) {
            temp.delete()
            MONUBackupInfo(
                id = id,
                createdAt = System.currentTimeMillis(),
                status = MONUBackupStatus.FAILED,
                scopes = scopes,
                locationDescription = error.message ?: "Backup failed."
            )
        }
    }

    @Synchronized
    fun restoreLatest(): MONUBackupInfo {
        val latest = listBackupFiles().maxByOrNull { it.lastModified() }
            ?: return MONUBackupInfo(
                id = "none",
                createdAt = null,
                status = MONUBackupStatus.NEVER_CREATED,
                scopes = scopes,
                locationDescription = "No backup is available."
            )

        return restore(latest)
    }

    @Synchronized
    fun restore(file: File): MONUBackupInfo {
        if (!file.exists()) {
            return MONUBackupInfo(
                id = file.nameWithoutExtension,
                createdAt = null,
                status = MONUBackupStatus.FAILED,
                scopes = scopes,
                locationDescription = "Backup file does not exist."
            )
        }

        return try {
            verifyZip(file)
            MONUDatabaseProvider.close()

            ZipFile(file).use { zip ->
                extractDirectory(zip, "shared_prefs", File(appContext.dataDir, "shared_prefs"))
                extractDirectory(zip, "databases", File(appContext.dataDir, "databases"))
            }

            MONUBackupInfo(
                id = file.nameWithoutExtension,
                createdAt = file.lastModified(),
                status = MONUBackupStatus.COMPLETED,
                scopes = scopes,
                locationDescription = "Restore completed. Restart the application.",
                filePath = file.absolutePath,
                checksum = sha256(file),
                sizeBytes = file.length()
            )
        } catch (error: Throwable) {
            MONUBackupInfo(
                id = file.nameWithoutExtension,
                createdAt = file.lastModified(),
                status = MONUBackupStatus.FAILED,
                scopes = scopes,
                locationDescription = error.message ?: "Restore failed.",
                filePath = file.absolutePath
            )
        }
    }

    fun listBackups(): List<MONUBackupInfo> {
        return listBackupFiles()
            .sortedByDescending { it.lastModified() }
            .map { file ->
                MONUBackupInfo(
                    id = file.nameWithoutExtension,
                    createdAt = file.lastModified(),
                    status = MONUBackupStatus.COMPLETED,
                    scopes = scopes,
                    locationDescription = file.absolutePath,
                    filePath = file.absolutePath,
                    checksum = runCatching { sha256(file) }.getOrNull(),
                    sizeBytes = file.length()
                )
            }
    }

    private fun listBackupFiles(): List<File> {
        return backupRoot
            .listFiles()
            ?.filter { it.isFile && it.extension == "zip" }
            .orEmpty()
    }

    private fun addDirectory(
        zip: ZipOutputStream,
        directory: File,
        root: String
    ) {
        if (!directory.exists() || !directory.isDirectory) return

        directory.walkTopDown()
            .filter { it.isFile }
            .forEach { file ->
                val relative = file.relativeTo(directory).invariantSeparatorsPath
                addFile(zip, file, "$root/$relative")
            }
    }

    private fun addFile(
        zip: ZipOutputStream,
        file: File,
        entryName: String
    ) {
        zip.putNextEntry(ZipEntry(entryName))
        BufferedInputStream(FileInputStream(file)).use { input ->
            input.copyTo(zip)
        }
        zip.closeEntry()
    }

    private fun addBytes(
        zip: ZipOutputStream,
        entryName: String,
        bytes: ByteArray
    ) {
        zip.putNextEntry(ZipEntry(entryName))
        zip.write(bytes)
        zip.closeEntry()
    }

    private fun verifyZip(file: File) {
        ZipFile(file).use { zip ->
            require(zip.getEntry("metadata.json") != null) {
                "Backup metadata is missing."
            }

            zip.entries().asSequence().forEach { entry ->
                val normalized = File(entry.name).normalize().path
                require(!normalized.startsWith("../") && normalized != "..") {
                    "Unsafe backup entry."
                }
            }
        }
    }

    private fun extractDirectory(
        zip: ZipFile,
        rootName: String,
        destination: File
    ) {
        destination.mkdirs()

        zip.entries().asSequence()
            .filter { !it.isDirectory && it.name.startsWith("$rootName/") }
            .forEach { entry ->
                val relative = entry.name.removePrefix("$rootName/")
                val target = File(destination, relative)

                val canonicalRoot = destination.canonicalFile
                val canonicalTarget = target.canonicalFile

                require(
                    canonicalTarget.path == canonicalRoot.path ||
                        canonicalTarget.path.startsWith(canonicalRoot.path + File.separator)
                ) {
                    "Unsafe restore path."
                }

                canonicalTarget.parentFile?.mkdirs()

                zip.getInputStream(entry).use { input ->
                    FileOutputStream(canonicalTarget).use { output ->
                        input.copyTo(output)
                    }
                }
            }
    }

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")

        FileInputStream(file).use { input ->
            val buffer = ByteArray(64 * 1024)

            while (true) {
                val count = input.read(buffer)
                if (count <= 0) break
                digest.update(buffer, 0, count)
            }
        }

        return digest.digest().joinToString("") {
            "%02x".format(it)
        }
    }
}
