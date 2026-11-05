package com.Tonkapa7.Piwpul.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readLong(key: String, fallback: Long): Long = prefs.getLong(key, fallback)

    fun writeLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun readBoolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun writeBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun readString(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun writeString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun clearProgress(keys: List<String>) {
        val editor = prefs.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }

    companion object {
        private const val STORE_NAME = "frozen_fishing_store"
    }
}
