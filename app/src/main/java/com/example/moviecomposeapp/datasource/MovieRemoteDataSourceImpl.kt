package com.example.moviecomposeapp.datasource

import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer
import com.example.moviecomposeapp.common.helpers.Response
import com.example.moviecomposeapp.common.helpers.apiCall
import com.example.moviecomposeapp.models.NetworkActorDetails
import com.example.moviecomposeapp.models.NetworkActorMovies
import com.example.moviecomposeapp.models.NetworkMovie
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val api: MovieApi,
) : MovieRemoteDataSource {

    override suspend fun getTrendingMoviesFirstPage(): Response<NetworkMovie> =
        apiCall { api.getTrendingMovies() }

    override suspend fun getTopRatedMoviesFirstPage(): Response<NetworkMovie> =
        apiCall { api.getTopRatedMovies() }

    override suspend fun getUpcomingMoviesFirstPage(): Response<NetworkMovie> =
        apiCall { api.getUpcomingMovies() }

    override suspend fun getMovieDetails(movieId: Int): Response<NetworkMovieDetail> =
        apiCall { api.getMovieDetails(movieId) }

    override suspend fun getMovieCredits(movieId: Int): Response<NetworkMovieCredit> =
        apiCall { api.getMovieCredits(movieId) }

    override suspend fun getMovieTrailers(movieId: Int): Response<NetworkMovieTrailer> =
        apiCall { api.getMovieTrailers(movieId) }

    override suspend fun getActorDetails(actorId: Int): Response<NetworkActorDetails> =
        apiCall { api.getActorDetails(actorId) }

    override suspend fun getActorMovies(actorId: Int): Response<NetworkActorMovies> =
        apiCall { api.getActorMovies(actorId) }
}