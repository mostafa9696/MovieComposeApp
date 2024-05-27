package com.example.moviecomposeapp.data.repo.appConfig

import com.example.moviecomposeapp.data.AppDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppConfigRepoImp @Inject constructor(
    private val appDataStore: AppDataStore
): AppConfigRepo {

    override suspend fun observeAppTheme(): Flow<Boolean> = appDataStore.observeAppTheme()

    override suspend fun updateAppTheme(isDarkMode: Boolean) =
        appDataStore.updateAppTheme(isDarkMode)

    override suspend fun observeDynamicColor(): Flow<Boolean> = appDataStore.observeDynamicColor()

    override suspend fun updateDynamicColor(dynamicColor: Boolean) =
        appDataStore.updateDynamicColor(dynamicColor)

    override suspend fun isUserLogin() = appDataStore.isUserLogin()
}