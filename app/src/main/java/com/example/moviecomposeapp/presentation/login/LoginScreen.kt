package com.example.moviecomposeapp.presentation.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviecomposeapp.common.component.AppDialog
import com.example.moviecomposeapp.common.component.AppScaffold
import com.example.moviecomposeapp.common.component.AuthBackground
import com.example.moviecomposeapp.common.component.AuthEmailOutlinedTextField
import com.example.moviecomposeapp.common.component.AuthPasswordOutlinedTextField
import com.example.moviecomposeapp.common.component.AuthWelcomeMessage
import com.example.moviecomposeapp.common.component.ButtonCircularProgressIndicator
import com.example.moviecomposeapp.common.component.MovieButton
import com.example.moviecomposeapp.common.component.MovieTextButton
import com.example.moviecomposeapp.common.component.ShowUserMessage
import com.example.moviecomposeapp.common.constants.ComponentDimens
import com.example.moviecomposeapp.common.constants.Dimens
import com.example.moviecomposeapp.common.helpers.DialogUiEvent


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onCreateAccountClick: () -> Unit,
    onNavigationToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.userMessages.isNotEmpty()) {
        ShowUserMessage(
            message = uiState.userMessages.first().asString(),
            consumedMessage = viewModel::emptyUserMessage
        )
    }

    if (uiState.dialogUiEvent != DialogUiEvent.InActive) {
        ForgetPasswordDialog(
            onDismiss = remember { viewModel::endResetPasswordDialog },
            onSendClick = remember { viewModel::sendPasswordResetMail },
            isLoading = uiState.dialogUiEvent == DialogUiEvent.Loading,
            emailValue = viewModel.passwordResetEmailValue,
            onEmailValueChange = remember { viewModel::updatePasswordResetMail },
            emailLabelText = uiState.passwordResetFieldErrorMessage?.asString()
                ?: "Email Address",
            isEmailFieldError = uiState.passwordResetFieldErrorMessage != null
        )
    }
    if (uiState.navigateToHome)
        onNavigationToHome()

    AppScaffold(modifier = Modifier.fillMaxWidth()) { paddingValues ->
        AuthBackground()
        Log.d("ww8", "padding $paddingValues" )
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LoginScreenContent(
                modifier = Modifier.padding(paddingValues),
                emailValue = viewModel.emailValue,
                onEmailValueChange = remember { viewModel::updateEmailValue },
                emailFieldError = uiState.emailFieldErrorMessage != null,
                emailFieldLabel = uiState.emailFieldErrorMessage?.asString()
                    ?: "Email",
                passwordValue = viewModel.passwordValue,
                onPasswordValueChange = remember { viewModel::updatePasswordValue },
                passwordFieldError = uiState.passwordFieldErrorMessage != null,
                passwordFieldLabel = uiState.passwordFieldErrorMessage?.asString()
                    ?: "Password",
                onLoginClick = remember { { viewModel.login() } },
                onCreateAccountClick = onCreateAccountClick,
                onForgotPasswordClick = remember { viewModel::startResetPasswordDialog },
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    emailFieldLabel: String,
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordFieldError: Boolean,
    passwordFieldLabel: String,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthWelcomeMessage(text = "Welcome to Movie app")
        AuthSection(
            modifier = Modifier.fillMaxWidth(),
            emailValue = emailValue,
            onEmailValueChange = onEmailValueChange,
            passwordValue = passwordValue,
            onPasswordValueChange = onPasswordValueChange,
            emailFieldError = emailFieldError,
            passwordFieldError = passwordFieldError,
            emailFieldLabel = emailFieldLabel,
            passwordFieldLabel = passwordFieldLabel,
            onForgotPasswordClick = onForgotPasswordClick,
            onLoginClick = onLoginClick
        )
        ContinueWith()
        SignUpNow(onCreateAccountClick = onCreateAccountClick)
    }
}

@Composable
private fun AuthSection(
    modifier: Modifier = Modifier,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    passwordFieldError: Boolean,
    emailFieldLabel: String,
    passwordFieldLabel: String,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AuthEmailOutlinedTextField(
            value = emailValue,
            onValueChange = onEmailValueChange,
            isError = emailFieldError,
            labelText = emailFieldLabel
        )
        AuthPasswordOutlinedTextField(
            value = passwordValue,
            onValueChange = onPasswordValueChange,
            isError = passwordFieldError,
            labelText = passwordFieldLabel
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            MovieTextButton(
                text = "Forget Password",
                onClick = onForgotPasswordClick
            )
        }
        MovieButton(
            modifier = Modifier
                .padding(top = Dimens.oneLevelPadding)
                .height(ComponentDimens.buttonHeight)
                .fillMaxWidth(),
            text = "Login",
            onClick = onLoginClick
        )

    }
}

@Composable
private fun ContinueWith() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.primary)
        Text(
            text = "Or continue with",
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun SignUpNow(onCreateAccountClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Don't have an account")
        Spacer(modifier = Modifier.width(4.dp))
        MovieTextButton(
            text = "Sign up",
            onClick = onCreateAccountClick,
            fontWeight = FontWeight.Bold,
            contentPadding = PaddingValues(0.dp)
        )
    }
}

@Composable
fun ForgetPasswordDialog(
    onDismiss: () -> Unit,
    onSendClick: () -> Unit,
    isLoading: Boolean,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailLabelText: String,
    isEmailFieldError: Boolean,
) {
    AppDialog(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.threeLevelPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = "To reset your password, enter the email address you use to sign in to app.")
            TextField(
                value = emailValue,
                onValueChange = onEmailValueChange,
                label = {
                    Text(text = emailLabelText)
                },
                isError = isEmailFieldError
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ElevatedButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(Dimens.twoLevelPadding))
                ElevatedButton(enabled = !isLoading, onClick = onSendClick) {
                    if (isLoading)
                        ButtonCircularProgressIndicator()
                    else
                        Text(text = "Send")
                }
            }
        }

    }
}