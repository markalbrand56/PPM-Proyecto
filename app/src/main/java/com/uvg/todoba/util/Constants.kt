package com.uvg.todoba.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Function to get the value of a preference
// Example: val name = dataStore.getPreference("name", "John Doe")
suspend fun <T> DataStore<Preferences>.getPreference(key: String, defaultValue: T): T {
    val preferences = this.data.first()
    return when (defaultValue) {
        is String -> preferences[stringPreferencesKey(key)] ?: defaultValue
        is Int -> preferences[intPreferencesKey(key)] ?: defaultValue
        is Boolean -> preferences[booleanPreferencesKey(key)] ?: defaultValue
        is Float -> preferences[floatPreferencesKey(key)] ?: defaultValue
        is Long -> preferences[longPreferencesKey(key)] ?: defaultValue
        else -> throw IllegalArgumentException("Not supported type")
    } as T
}

suspend fun DataStore<Preferences>.setPreference(key: String, value: String) {
    this.edit { preferences ->
        preferences[stringPreferencesKey(key)] = value
    }
}