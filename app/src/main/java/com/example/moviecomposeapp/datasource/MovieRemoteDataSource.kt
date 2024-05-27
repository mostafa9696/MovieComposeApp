package com.example.moviecomposeapp.datasource

import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer
import com.example.moviecomposeapp.common.helpers.Response
import com.example.moviecomposeapp.models.NetworkActorDetails
import com.example.moviecomposeapp.models.NetworkActorMovies
import com.example.moviecomposeapp.models.NetworkMovie

interface MovieRemoteDataSource {

    suspend fun getTrendingMoviesFirstPage(): Response<NetworkMovie>

    suspend fun getTopRatedMoviesFirstPage(): Response<NetworkMovie>

    suspend fun getUpcomingMoviesFirstPage(): Response<NetworkMovie>

    suspend fun getMovieDetails(movieId: Int): Response<NetworkMovieDetail>

    suspend fun getMovieCredits(movieId: Int): Response<NetworkMovieCredit>

    suspend fun getMovieTrailers(movieId: Int): Response<NetworkMovieTrailer>

    suspend fun getActorDetails(actorId: Int): Response<NetworkActorDetails>

    suspend fun getActorMovies(actorId: Int): Response<NetworkActorMovies>
}