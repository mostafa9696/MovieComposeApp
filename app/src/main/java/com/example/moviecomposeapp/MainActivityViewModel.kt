package com.example.moviecomposeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.common.connectivity.ConnectivityObserver
import com.example.moviecomposeapp.common.connectivity.NetworkConnectivityObserver
import com.example.moviecomposeapp.data.repo.appConfig.AppConfigRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val repository: AppConfigRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        isUserLogin()
        initializeTheme()
        observeTheme()
        observeDynamicColor()
        observeNetworkConnectivity()
    }

    private fun isUserLogin() {
        viewModelScope.launch(ioDispatcher) {
            repository.isUserLogin().collect { isLogin ->
                _uiState.update {
                    it.copy(isUserLogin = isLogin)
                }
            }
        }
    }

    private fun initializeTheme() {
        runBlocking(ioDispatcher) {
            _uiState.update {
                it.copy(
                    isDarkModeOn = repository.observeAppTheme().first(),
                    isDynamicColorOn = repository.observeDynamicColor().first()
                )
            }
        }
    }

    private fun observeTheme() {
        viewModelScope.launch(ioDispatcher) {
            repository.observeAppTheme().collect { isDarkMode ->
                _uiState.update {
                    it.copy(isDarkModeOn = isDarkMode)
                }
            }
        }
    }

    private fun observeDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            repository.observeDynamicColor().collect { isDynamicColorOn ->
                _uiState.update {
                    it.copy(isDynamicColorOn = isDynamicColorOn)
                }
            }
        }
    }

    private fun observeNetworkConnectivity() {
        viewModelScope.launch(ioDispatcher) {
            networkConnectivityObserver.observer().collect { networkStatus ->
                when (networkStatus) {
                    ConnectivityObserver.Status.Available -> {}

                    ConnectivityObserver.Status.Unavailable -> {
                        _uiState.update {
                            it.copy(userMessages = listOf(UiText.DynamicString("Error happened, try again")))
                        }
                    }

                    ConnectivityObserver.Status.Lost -> {
                        _uiState.update {
                            it.copy(userMessages = listOf(UiText.DynamicString("Check internet connection")))
                        }
                    }
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

data class MainUiState(
    val isDarkModeOn: Boolean = true,
    val isDynamicColorOn: Boolean = false,
    val isUserLogin: Boolean = false,
    val userMessages: List<UiText> = emptyList(),
)