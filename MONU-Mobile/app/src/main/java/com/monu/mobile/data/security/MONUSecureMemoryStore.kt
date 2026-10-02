package com.monu.mobile.data.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.io.FileOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class MONUSecureMemoryStore(
    context: Context
) {

    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "monu_memory_aes_key_v1"
        private const val FILE_NAME = "monu_memory.enc"
        private const val TEMP_FILE_NAME = "monu_memory.enc.tmp"

        private const val VERSION: Byte = 1
        private const val IV_LENGTH = 12
        private const val TAG_LENGTH_BITS = 128

        private const val MAX_FILE_BYTES = 512 * 1024
    }

    private val appContext = context.applicationContext

    private val encryptedFile =
        File(appContext.filesDir, FILE_NAME)

    private val temporaryFile =
        File(appContext.filesDir, TEMP_FILE_NAME)

    private fun getOrCreateKey(): SecretKey {
        val keyStore =
            KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
                load(null)
            }

        val existing =
            keyStore.getKey(KEY_ALIAS, null)

        if (existing is SecretKey) {
            return existing
        }

        val generator =
            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_PROVIDER
            )

        val spec =
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(
                    KeyProperties.BLOCK_MODE_GCM
                )
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE
                )
                .setKeySize(256)
                .build()

        generator.init(spec)

        return generator.generateKey()
    }

    @Synchronized
    fun read(): String? {
        if (!encryptedFile.exists()) {
            return null
        }

        return try {
            val data = encryptedFile.readBytes()

            if (data.size < 1 + IV_LENGTH + 1) {
                return null
            }

            if (data.size > MAX_FILE_BYTES) {
                return null
            }

            if (data[0] != VERSION) {
                return null
            }

            val ivStart = 1
            val ivEnd = ivStart + IV_LENGTH

            val iv =
                data.copyOfRange(
                    ivStart,
                    ivEnd
                )

            val ciphertext =
                data.copyOfRange(
                    ivEnd,
                    data.size
                )

            val cipher =
                Cipher.getInstance("AES/GCM/NoPadding")

            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(
                    TAG_LENGTH_BITS,
                    iv
                )
            )

            cipher.doFinal(ciphertext)
                .toString(Charsets.UTF_8)

        } catch (_: Exception) {
            null
        }
    }

    @Synchronized
    fun write(value: String): Boolean {
        return try {
            val plaintext =
                value.toByteArray(Charsets.UTF_8)

            val cipher =
                Cipher.getInstance("AES/GCM/NoPadding")

            cipher.init(
                Cipher.ENCRYPT_MODE,
                getOrCreateKey()
            )

            val iv =
                cipher.iv

            val ciphertext =
                cipher.doFinal(plaintext)

            val output =
                ByteArray(
                    1 +
                        iv.size +
                        ciphertext.size
                )

            output[0] = VERSION

            System.arraycopy(
                iv,
                0,
                output,
                1,
                iv.size
            )

            System.arraycopy(
                ciphertext,
                0,
                output,
                1 + iv.size,
                ciphertext.size
            )

            if (output.size > MAX_FILE_BYTES) {
                return false
            }

            FileOutputStream(
                temporaryFile,
                false
            ).use { stream ->
                stream.write(output)
                stream.fd.sync()
            }

            if (!temporaryFile.renameTo(encryptedFile)) {
                temporaryFile.delete()
                return false
            }

            true

        } catch (_: Exception) {
            temporaryFile.delete()
            false
        }
    }

    @Synchronized
    fun clear(): Boolean {
        return try {
            temporaryFile.delete()
            encryptedFile.delete()
        } catch (_: Exception) {
            false
        }
    }
}
