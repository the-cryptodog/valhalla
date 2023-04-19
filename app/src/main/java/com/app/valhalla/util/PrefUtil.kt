package com.app.valhalla.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val USER_PREFERENCES_NAME = "bye_preferences"

val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)

class PrefUtil(context: Context) {

    private val dataStore = context.dataStore

    suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun getString(key: String, defaultValue: String = ""): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: defaultValue
    }

    suspend fun isEmpty(key: String): Boolean {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)].isNullOrEmpty()
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }
}

