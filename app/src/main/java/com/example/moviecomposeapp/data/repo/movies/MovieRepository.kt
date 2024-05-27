package com.example.moviecomposeapp.data.repo.movies

import androidx.paging.PagingData
import com.example.moviecomposeapp.common.helpers.Response
import com.example.moviecomposeapp.models.ActorDetails
import com.example.moviecomposeapp.models.ActorMovies
import com.example.moviecomposeapp.models.Movie
import com.example.moviecomposeapp.models.MovieContent
import com.example.moviecomposeapp.models.MovieCredit
import com.example.moviecomposeapp.models.MovieDetail
import com.example.moviecomposeapp.models.MovieTrailer
import com.example.moviecomposeapp.models.RecommendedMovieContent
import com.example.moviecomposeapp.models.UserReviewResults
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getTrendingMoviesFirstPage(): Response<Movie>

    suspend fun getTopRatedMoviesFirstPage(): Response<Movie>

    suspend fun getUpcomingMoviesFirstPage(): Response<Movie>

    fun getAllTrendingMovies(): Flow<PagingData<MovieContent>>

    fun getAllTopRatedMovies(): Flow<PagingData<MovieContent>>

    fun getAllUpcomingMovies(): Flow<PagingData<MovieContent>>

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetail>

    suspend fun getMovieCredits(movieId: Int): Response<MovieCredit>

    suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer>

    fun searchMovie(query: String): Flow<PagingData<MovieContent>>

    suspend fun getActorDetails(actorId: Int): Response<ActorDetails>

    suspend fun getActorMovies(actorId: Int): Response<ActorMovies>

    fun getUserMovieReviews(movieId: Int): Flow<PagingData<UserReviewResults>>

    fun getMovieRecommendations(movieId: Int): Flow<PagingData<RecommendedMovieContent>>
}