package com.example.currencyexchange.mock

import android.content.SharedPreferences

class MockSharedPreferences: SharedPreferences {
    var booleanValue: Boolean = false
    var stringValue: String = ""
    var intValue: Int = 0
    var longValue: Long = 0
    var floatValue: Float = 0f
    var stringSetValue: MutableSet<String> = mutableSetOf()

    override fun contains(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBoolean(p0: String?, p1: Boolean): Boolean {
        return booleanValue
    }

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun getInt(p0: String?, p1: Int): Int {
        return intValue
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("Not yet implemented")
    }

    override fun edit(): SharedPreferences.Editor {
        return MockSharedPreferencesEditor()
    }

    override fun getLong(p0: String?, p1: Long): Long {
        return longValue
    }

    override fun getFloat(p0: String?, p1: Float): Float {
        return floatValue
    }

    override fun getStringSet(p0: String?, p1: MutableSet<String>?): MutableSet<String> {
        return stringSetValue
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun getString(p0: String?, p1: String?): String? {
        return stringValue
    }
}

class MockSharedPreferencesEditor: SharedPreferences.Editor {
    override fun clear(): SharedPreferences.Editor {
        return this
    }

    override fun putLong(p0: String?, p1: Long): SharedPreferences.Editor {
        return this
    }

    override fun putInt(p0: String?, p1: Int): SharedPreferences.Editor {
        return this
    }

    override fun remove(p0: String?): SharedPreferences.Editor {
        return this
    }

    override fun putBoolean(p0: String?, p1: Boolean): SharedPreferences.Editor {
        return this
    }

    override fun putStringSet(p0: String?, p1: MutableSet<String>?): SharedPreferences.Editor {
        return this
    }

    override fun commit(): Boolean {
        return true
    }

    override fun putFloat(p0: String?, p1: Float): SharedPreferences.Editor {
        return this
    }

    override fun apply() {
        // do nothing
    }

    override fun putString(p0: String?, p1: String?): SharedPreferences.Editor {
        return this
    }

}