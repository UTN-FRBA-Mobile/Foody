package ar.edu.utn.frba.foody.ui.dataBase.StoreUserSession

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserSession(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserSession")
        val USER_SESSION_KEY = stringPreferencesKey("user_session")
    }

    val getSession: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_SESSION_KEY] ?: ""
        }

    suspend fun saveSession(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_SESSION_KEY] = name
        }
    }

    suspend fun deleteSession(){
        context.dataStore.edit {
            it.remove(USER_SESSION_KEY)
        }
    }
}