package com.example.moviecomposeapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.UiText
import com.example.moviecomposeapp.common.helpers.DialogUiEvent
import com.example.moviecomposeapp.common.utils.AuthInputChecker
import com.example.moviecomposeapp.common.utils.isValidEmail
import com.example.moviecomposeapp.data.repo.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var emailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    var passwordResetEmailValue by mutableStateOf("")
        private set

    fun updateEmailValue(value: String) {
        emailValue = value
    }

    fun updatePasswordValue(value: String) {
        passwordValue = value
    }

    fun updatePasswordResetMail(value: String) {
        passwordResetEmailValue = value
    }

    fun sendPasswordResetMail() {
        if (passwordResetEmailValue.isValidEmail()) {
            _uiState.update {
                it.copy(dialogUiEvent = DialogUiEvent.Loading)
            }
        } else {
            _uiState.update {
                it.copy(passwordFieldErrorMessage = UiText.DynamicString("Enter valid email"))
            }
        }
    }

    fun login() {
        val isEmailValid = AuthInputChecker.checkEmailField(
            email = emailValue,
            onBlank = { error ->
                _uiState.update {
                    it.copy(emailFieldErrorMessage = error)
                }
            },
            onUnValid = { errorMessage ->
                _uiState.update {
                    it.copy(emailFieldErrorMessage = errorMessage)
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = null)
                }
            }
        )

        val isPasswordValid = AuthInputChecker.checkPasswordField(
            password = passwordValue,
            onBlank = { errorMessage ->
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = errorMessage)
                }
            },
            onUnValid = { errorMessage ->
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = errorMessage)
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = null)
                }
            }
        )

        if (isEmailValid && isPasswordValid) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                repository.setUserLogin(true)
                _uiState.update {
                    it.copy(navigateToHome = true)
                }
            }
        }
    }

    fun emptyUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }

    fun startResetPasswordDialog() {
        _uiState.update {
            it.copy(dialogUiEvent = DialogUiEvent.Active)
        }
    }

    fun endResetPasswordDialog() {
        _uiState.update {
            it.copy(dialogUiEvent = DialogUiEvent.InActive)
        }
    }
}


data class LoginUiState(
    val isLoading: Boolean = false,
    val navigateToHome: Boolean = false,
    val userMessages: List<UiText> = emptyList(),
    val emailFieldErrorMessage: UiText? = null,
    val passwordFieldErrorMessage: UiText? = null,
    val passwordResetFieldErrorMessage: UiText? = null,
    val dialogUiEvent: DialogUiEvent = DialogUiEvent.InActive,
)