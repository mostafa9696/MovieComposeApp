package com.example.moviecomposeapp.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.data.repo.movies.MovieRepository
import com.example.moviecomposeapp.models.MovieContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    var query by mutableStateOf("")
        private set

    fun updateQueryValue(value: String) {
        query = value
    }

    fun search() {
        if (query.isNotBlank()) {
            _uiState.update {
                it.copy(
                    queryFieldErrorMessage = null,
                    isSearchDone = true,
                    searchResult = repository.searchMovie(query).cachedIn(viewModelScope)
                )
            }
        } else
            _uiState.update {
                it.copy(queryFieldErrorMessage = UiText.DynamicString("Enter valid search input"))
            }
    }
}

data class SearchUiState(
    val isSearchDone: Boolean = false,
    val searchResult: Flow<PagingData<MovieContent>> = emptyFlow(),
    val queryFieldErrorMessage: UiText? = null,
)