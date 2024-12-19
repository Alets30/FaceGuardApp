package com.example.faceguardapp.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IsStaffStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserPreferences")
        val IS_STAFF = booleanPreferencesKey("is_staff")
    }

    val getIsStaff: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_STAFF] ?: false
    }

    suspend fun saveIsStaff(isStaff: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_STAFF] = isStaff
        }
    }
}