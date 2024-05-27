package com.example.moviecomposeapp.data.repo.appConfig

import kotlinx.coroutines.flow.Flow

interface AppConfigRepo {

    suspend fun observeAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(isDarkMode: Boolean)

    suspend fun observeDynamicColor(): Flow<Boolean>

    suspend fun updateDynamicColor(dynamicColor: Boolean)

    suspend fun isUserLogin(): Flow<Boolean>

}