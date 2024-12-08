import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsernameStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Username")
        val USERNAME = stringPreferencesKey("username")
    }

    val getUsername: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    suspend fun saveUsername(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = token
        }
    }
}