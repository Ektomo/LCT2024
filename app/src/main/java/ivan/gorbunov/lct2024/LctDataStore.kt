package ivan.gorbunov.lct2024

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ivan.gorbunov.lct2024.ui.screens.auth.login.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LctDataStore @Inject constructor(private val context: Context) {


    val getUserId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY] ?: ""
        }

    suspend fun saveUserId(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = name
        }
    }

    val role: Flow<Role?> = context.dataStore.data
        .map {prefs ->
            when(prefs[ROLE]?.lowercase()){
                "client" -> Role.Client
                "coach" -> Role.Coach
                else -> null
            }
        }

    suspend fun setRole(role: Role){
        context.dataStore.edit { prefs ->
            prefs[ROLE] = role.name
        }
    }

    val isLogged: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[IS_LOGGED_IN] ?: false
        }

    suspend fun setIsLogged(value: Boolean){
        context.dataStore.edit {
            it[IS_LOGGED_IN] = value
        }
    }


    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("lct")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val ROLE = stringPreferencesKey("role")
    }

}