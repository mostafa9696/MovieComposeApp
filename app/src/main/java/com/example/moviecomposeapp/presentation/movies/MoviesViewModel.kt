package com.example.moviecomposeapp.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.common.helpers.Response
import com.example.moviecomposeapp.data.repo.movies.MovieRepository
import com.example.moviecomposeapp.models.MovieContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val moviesRepository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        getAllDataAndUpdateUi()
    }

    private fun getAllDataAndUpdateUi() {
        viewModelScope.launch(ioDispatcher) {
            val trendingMoviesDeferred = async { moviesRepository.getTrendingMoviesFirstPage() }
            val topRatedMoviesDeferred = async { moviesRepository.getTopRatedMoviesFirstPage() }
            val upcomingMoviesDeferred = async { moviesRepository.getUpcomingMoviesFirstPage() }

            val trendingMoviesResponse = trendingMoviesDeferred.await()
            val topRatedMoviesResponse = topRatedMoviesDeferred.await()
            val upcomingMoviesResponse = upcomingMoviesDeferred.await()

            if (trendingMoviesResponse is Response.Success &&
                topRatedMoviesResponse is Response.Success &&
                upcomingMoviesResponse is Response.Success
            ) {
                _uiState.update {
                    it.copy(
                        trendingMovies = trendingMoviesResponse.data.movies,
                        topRatedMovies = topRatedMoviesResponse.data.movies,
                        upcomingMovies = upcomingMoviesResponse.data.movies,
                        movieDataStatus = MovieDataStatus.Success
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        movieDataStatus = MovieDataStatus.Error(
                            message = UiText.DynamicString("Please try again later")
                        )
                    )
                }
            }
        }
    }
}


data class MoviesUiState(
    val movieDataStatus: MovieDataStatus = MovieDataStatus.Loading,
    val trendingMovies: List<MovieContent> = emptyList(),
    val topRatedMovies: List<MovieContent> = emptyList(),
    val upcomingMovies: List<MovieContent> = emptyList(),
)

sealed interface MovieDataStatus {
    object Loading : MovieDataStatus
    object Success : MovieDataStatus
    data class Error(val message: UiText) : MovieDataStatus
}