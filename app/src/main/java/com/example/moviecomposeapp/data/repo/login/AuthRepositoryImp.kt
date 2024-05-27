package com.example.moviecomposeapp.data.repo.login

import com.example.moviecomposeapp.data.AppDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
 private val appDataStore: AppDataStore
): AuthRepository {


    override suspend fun isUserLogin(): Flow<Boolean> {
        return appDataStore.isUserLogin()
    }

    override suspend fun setUserLogin(isLogin: Boolean) {
        appDataStore.updateUserLogin(isLogin)
    }
}