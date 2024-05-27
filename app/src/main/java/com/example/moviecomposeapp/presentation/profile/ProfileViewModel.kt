package com.example.moviecomposeapp.presentation.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.data.repo.appConfig.AppConfigRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: AppConfigRepo,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeAppTheme()
        observeDynamicColor()
        _uiState.update {
            it.copy(userEmail = "mostafa@test.com")
        }
    }

    var password by mutableStateOf("")
        private set

    fun updatePasswordValue(value: String) {
        password = value
    }


    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }


    fun setTheme(darkTheme: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            repository.updateAppTheme(darkTheme)
        }
    }

    private fun observeAppTheme() {
        viewModelScope.launch(ioDispatcher) {
            repository.observeAppTheme().collect { darkMode ->
                _uiState.update {
                    it.copy(isDarkModeOn = darkMode)
                }
            }
        }
    }

    fun setDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            repository.updateDynamicColor(dynamicColor)
        }
    }

    private fun observeDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            repository.observeDynamicColor().collect { dynamicColor ->
                _uiState.update {
                    it.copy(isDynamicColorOn = dynamicColor)
                }
            }
        }
    }


}

data class ProfileUiState(
    val profileImgUri: Uri? = null,
    val userEmail: String = "",
    val userMessages: List<UiText> = emptyList(),
    val isProfileImageUploading: Boolean = false,
    val isDarkModeOn: Boolean = false,
    val isDynamicColorOn: Boolean = false,
)