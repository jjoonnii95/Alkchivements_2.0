package de.daschubbm.alkchievements20.dataManagement

import android.content.Context
import de.daschubbm.alkchievements20.App

object LocalData {
    val prefs = App.context?.getSharedPreferences("alk", Context.MODE_PRIVATE) 
            ?: throw IllegalStateException("App context not ready!???!??")
    
    fun saveI(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun saveS(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun saveF(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun saveL(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }

    fun saveB(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun loadI(key: String, defaultVal: Int = -1): Int {
        return prefs.getInt(key, defaultVal)
    }

    fun loadS(key: String, defaultVal: String = ""): String {
        return prefs.getString(key, defaultVal)
    }

    fun loadF(key: String, defaultVal: Float = -1f): Float {
        return prefs.getFloat(key, defaultVal)
    }

    fun loadL(key: String, defaultVal: Set<String> = emptySet()): Set<String> {
        return prefs.getStringSet(key, defaultVal)
    }

    fun loadB(key: String, defaultVal: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultVal)
    }
}