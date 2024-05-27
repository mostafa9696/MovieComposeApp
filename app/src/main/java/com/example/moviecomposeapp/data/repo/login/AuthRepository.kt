package com.example.moviecomposeapp.data.repo.login

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun isUserLogin(): Flow<Boolean>

    suspend fun setUserLogin(isLogin: Boolean)
}