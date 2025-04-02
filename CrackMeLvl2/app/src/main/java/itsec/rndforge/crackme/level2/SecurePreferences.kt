package itsec.rndforge.crackme.level2

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Created by mzennis on 23/01/2025
 */
class SecurePreferences private constructor(context: Context, prefName: String) {

    companion object {

        const val KEY = "abcde"

        fun create(context: Context): SecurePreferences {
            return SecurePreferences(context, "android_prefs")
        }
    }

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            prefName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
}