package com.example.moviecomposeapp.presentation.movie_details

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.common.helpers.Response
import com.example.moviecomposeapp.data.repo.movies.MovieRepository
import com.example.moviecomposeapp.models.Cast
import com.example.moviecomposeapp.models.MovieDetail
import com.example.moviecomposeapp.models.RecommendedMovieContent
import com.example.moviecomposeapp.models.Trailer
import com.example.moviecomposeapp.models.UserReviewResults
import com.example.moviecomposeapp.navigaton.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private var movieName: String = ""

    init {
        val movieId = savedStateHandle.get<String>(MainDestinations.MOVIE_DETAILS_ID_KEY)

        movieId?.let { id ->
            getAllDataAndUpdateUi(id.toInt())
        }
    }

    private fun getAllDataAndUpdateUi(movieId: Int) {
        viewModelScope.launch(ioDispatcher) {
            val movieDetailsDeferred = async { repository.getMovieDetails(movieId) }
            val movieCreditsDeferred = async { repository.getMovieCredits(movieId) }
            val movieTrailersDeferred = async { repository.getMovieTrailers(movieId) }

            val movieDetailsResponse = movieDetailsDeferred.await()
            val movieCreditsResponse = movieCreditsDeferred.await()
            val movieTrailersResponse = movieTrailersDeferred.await()

            if (movieDetailsResponse is Response.Success &&
                movieCreditsResponse is Response.Success &&
                movieTrailersResponse is Response.Success
            ) {
                _uiState.update {
                    it.copy(
                        movieDetails = listOf(movieDetailsResponse.data),
                        movieCast = movieCreditsResponse.data.cast,
                        movieTrailers = movieTrailersResponse.data.trailers,
                        userReviews = repository.getUserMovieReviews(movieId),
                        movieRecommendations = repository.getMovieRecommendations(movieId),
                        directorName = movieCreditsResponse.data.directorName,
                        movieDataStatus = MovieDataStatus.Success
                    )
                }

                movieName = movieDetailsResponse.data.movieName
            } else {
                _uiState.update {
                    it.copy(
                        movieDataStatus = MovieDataStatus.Error(
                            message = UiText.DynamicString("Error in fetching movie data")
                        )
                    )
                }
            }
        }
    }



    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }
}

data class  MovieDetailUiState(
    val movieDetails: List<MovieDetail> = emptyList(),
    val movieTrailers: List<Trailer> = emptyList(),
    val movieCast: List<Cast> = emptyList(),
    val userReviews: Flow<PagingData<UserReviewResults>> = emptyFlow(),
    val movieRecommendations: Flow<PagingData<RecommendedMovieContent>> = emptyFlow(),
    val movieDataStatus: MovieDataStatus = MovieDataStatus.Loading,
    val directorName: String = "",
    val userMessages: List<UiText> = emptyList(),
    val isWatchlistButtonInProgress: Boolean = false,
    val isMovieInWatchList: Boolean = false,
    val gemini: Gemini = Gemini(),
    val posterBackgroundColors: List<Color> = listOf(
        Color.Transparent,
        Color.Transparent
    ),
)

sealed interface MovieDataStatus {
    object Loading : MovieDataStatus
    object Success : MovieDataStatus
    class Error(val message: UiText) : MovieDataStatus
}

data class Gemini(
    val isLoading: Boolean = false,
    val responseString: String? = null,
    val errorMessage: UiText? = null,
)