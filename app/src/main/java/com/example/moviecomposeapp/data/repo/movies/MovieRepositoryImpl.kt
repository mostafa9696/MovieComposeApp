package com.example.moviecomposeapp.data.repo.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.moviecomposeapp.common.helpers.MoviesPagingSource
import com.example.moviecomposeapp.common.helpers.RecommendedMoviesPagingSource
import com.example.moviecomposeapp.common.helpers.Response
import com.example.moviecomposeapp.common.helpers.SearchMoviesPagingSource
import com.example.moviecomposeapp.common.helpers.UserMovieReviewsPagingSource
import com.example.moviecomposeapp.common.utils.mapResponse
import com.example.moviecomposeapp.datasource.MovieApi
import com.example.moviecomposeapp.datasource.MovieRemoteDataSource
import com.example.moviecomposeapp.mapper.toActorDetails
import com.example.moviecomposeapp.mapper.toActorMovies
import com.example.moviecomposeapp.mapper.toMovie
import com.example.moviecomposeapp.mapper.toMovieContent
import com.example.moviecomposeapp.mapper.toMovieCredit
import com.example.moviecomposeapp.mapper.toMovieDetail
import com.example.moviecomposeapp.mapper.toMovieTrailer
import com.example.moviecomposeapp.mapper.toRecommendedMovieContent
import com.example.moviecomposeapp.mapper.toUserReviewResults
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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val api: MovieApi,
) : MovieRepository {

    override suspend fun getTrendingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getTrendingMoviesFirstPage().mapResponse { it.toMovie() }

    override suspend fun getTopRatedMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getTopRatedMoviesFirstPage().mapResponse { it.toMovie() }

    override suspend fun getUpcomingMoviesFirstPage(): Response<Movie> =
        movieRemoteDataSource.getUpcomingMoviesFirstPage().mapResponse { it.toMovie() }

    override fun getAllTrendingMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getTrendingMovies(page = currentPageNumber)
            }
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
    }

    override fun getAllTopRatedMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getTopRatedMovies(page = currentPageNumber)
            }
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
    }

    override fun getAllUpcomingMovies(): Flow<PagingData<MovieContent>> {
        val moviesPagingSource = MoviesPagingSource(
            apiCall = { currentPageNumber ->
                api.getUpcomingMovies(page = currentPageNumber)
            }
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { moviesPagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetail> =
        movieRemoteDataSource.getMovieDetails(movieId).mapResponse { it.toMovieDetail() }

    override suspend fun getMovieCredits(movieId: Int): Response<MovieCredit> =
        movieRemoteDataSource.getMovieCredits(movieId).mapResponse { it.toMovieCredit() }

    override suspend fun getMovieTrailers(movieId: Int): Response<MovieTrailer> =
        movieRemoteDataSource.getMovieTrailers(movieId).mapResponse { it.toMovieTrailer() }

    override fun searchMovie(query: String): Flow<PagingData<MovieContent>> {
        val searchMoviePagingSource = SearchMoviesPagingSource(
            query = query,
            api = api
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { searchMoviePagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toMovieContent()
            }
        }
    }

    override suspend fun getActorDetails(actorId: Int): Response<ActorDetails> =
        movieRemoteDataSource.getActorDetails(actorId).mapResponse { it.toActorDetails() }

    override suspend fun getActorMovies(actorId: Int): Response<ActorMovies> =
        movieRemoteDataSource.getActorMovies(actorId).mapResponse { it.toActorMovies() }

    override fun getUserMovieReviews(movieId: Int): Flow<PagingData<UserReviewResults>> {
        val userReviewsPagingSource = UserMovieReviewsPagingSource(
            movieId = movieId,
            api = api
        )

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { userReviewsPagingSource }
        ).flow.map {
            it.map { networkUserReviewResults ->
                networkUserReviewResults.toUserReviewResults()
            }
        }
    }

    override fun getMovieRecommendations(movieId: Int): Flow<PagingData<RecommendedMovieContent>> {
        val recommendedMoviesPagingSource = RecommendedMoviesPagingSource(
            api = api,
            movieId = movieId
        )
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { recommendedMoviesPagingSource }
        ).flow.map {
            it.map { networkMovieContent ->
                networkMovieContent.toRecommendedMovieContent()
            }
        }
    }
}