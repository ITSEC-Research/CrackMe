package itsec.rndforge.crackme.level3

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File

class SecureFileHandler private constructor(
    private val context: Context,
    private val fileName: String
) {

    companion object {
        fun create(context: Context): SecureFileHandler {
            return SecureFileHandler(context, "android_forge")
        }
    }

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedFile: EncryptedFile by lazy {
        val file = File(context.filesDir, fileName)
        EncryptedFile.Builder(
            context,
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    fun save(string: String) {
        encryptedFile.openFileOutput().use { outputStream ->
            outputStream.write(string.toByteArray())
        }
    }

    fun read(): String {
        return encryptedFile.openFileInput().use { inputStream ->
            inputStream.bufferedReader().use { it.readText() }
        }
    }
}