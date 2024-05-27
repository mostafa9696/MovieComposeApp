package com.example.moviecomposeapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.moviecomposeapp.data.AppDataStore.PreferencesKeys.APP_THEME
import com.example.moviecomposeapp.data.AppDataStore.PreferencesKeys.DYNAMIC_COLOR
import com.example.moviecomposeapp.data.AppDataStore.PreferencesKeys.IS_LOGIN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDataStore @Inject constructor(
    private val datastore: DataStore<Preferences>,
) {

    private object PreferencesKeys {
        val IS_LOGIN = booleanPreferencesKey("is_login")
        val APP_THEME = booleanPreferencesKey("app_theme")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    }

    suspend fun isUserLogin(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[IS_LOGIN] ?: false
        }
    }

    suspend fun updateUserLogin(isLogin: Boolean) {
        datastore.edit { preferences ->
            preferences[IS_LOGIN] = isLogin
        }
    }

    suspend fun observeAppTheme(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[APP_THEME] ?: true
        }
    }

    suspend fun updateAppTheme(darkMode: Boolean) {
        datastore.edit { preferences ->
            preferences[APP_THEME] = darkMode
        }
    }

    suspend fun observeDynamicColor(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[DYNAMIC_COLOR] ?: false
        }
    }

    suspend fun updateDynamicColor(dynamicColor: Boolean) {
        datastore.edit { preferences ->
            preferences[DYNAMIC_COLOR] = dynamicColor
        }
    }
}