package com.example.moviecomposeapp.di

import com.example.moviecomposeapp.data.repo.appConfig.AppConfigRepo
import com.example.moviecomposeapp.data.repo.appConfig.AppConfigRepoImp
import com.example.moviecomposeapp.data.repo.login.AuthRepository
import com.example.moviecomposeapp.data.repo.login.AuthRepositoryImp
import com.example.moviecomposeapp.data.repo.movies.MovieRepository
import com.example.moviecomposeapp.data.repo.movies.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindLoginRepository(repo: AuthRepositoryImp): AuthRepository

    @Binds
    abstract fun bindAppConfigRepository(repo: AppConfigRepoImp): AppConfigRepo

    @Binds
    abstract fun bindMoviesRepository(repo: MovieRepositoryImpl): MovieRepository
}