package com.example.moviecomposeapp.di

import com.example.moviecomposeapp.datasource.MovieRemoteDataSource
import com.example.moviecomposeapp.datasource.MovieRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindMoviesDataSource(repo: MovieRemoteDataSourceImpl): MovieRemoteDataSource
}