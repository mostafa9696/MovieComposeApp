package com.example.moviecomposeapp.presentation.see_all

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.common.constants.SeeAllType
import com.example.moviecomposeapp.data.repo.movies.MovieRepository
import com.example.moviecomposeapp.models.MovieContent
import com.example.moviecomposeapp.navigaton.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeeAllUiState())
    val uiState: StateFlow<SeeAllUiState> = _uiState.asStateFlow()

    init {
        val seeAllType = savedStateHandle.get<String>(MainDestinations.SEE_ALL_TYPE_KEY)

        when (seeAllType) {
            SeeAllType.TRENDING.toString() -> {
                _uiState.update {
                    it.copy(
                        topBarTitle = UiText.DynamicString("Trending"),
                        movieList = repository.getAllTrendingMovies().cachedIn(viewModelScope)
                    )
                }
            }

            SeeAllType.TOP_RATED.toString() -> {
                _uiState.update {
                    it.copy(
                        topBarTitle = UiText.DynamicString("Top Rated"),
                        movieList = repository.getAllTopRatedMovies().cachedIn(viewModelScope)
                    )
                }
            }

            SeeAllType.UPCOMING.toString() -> {
                _uiState.update {
                    it.copy(
                        topBarTitle = UiText.DynamicString("Upcomming"),
                        movieList = repository.getAllUpcomingMovies().cachedIn(viewModelScope)
                    )
                }
            }
        }
    }
}

data class SeeAllUiState(
    val movieList: Flow<PagingData<MovieContent>> = emptyFlow(),
    val topBarTitle: UiText = UiText.DynamicString(""),
)