package com.example.moviecomposeapp.common.utils

import com.ahmetocak.common.helpers.UiText

object AuthInputChecker {

    fun checkEmailField(
        email: String,
        onBlank: (UiText) -> Unit,
        onUnValid: (UiText) -> Unit,
        onSuccess: () -> Unit,
    ): Boolean {
        return if (email.isBlank()) {
            onBlank(UiText.DynamicString("Please fill in this field"))
            false
        } else if (!email.isValidEmail()) {
            onUnValid(UiText.DynamicString("Enter valid email"))
            false
        } else {
            onSuccess()
            true
        }
    }

    fun checkPasswordField(
        password: String,
        onBlank: (UiText) -> Unit,
        onUnValid: (UiText) -> Unit,
        onSuccess: () -> Unit,
    ): Boolean {
        return if (password.isBlank()) {
            onBlank(UiText.DynamicString("Please fill in this field"))
            false
        } else if (password.length < 6) {
            onUnValid(UiText.DynamicString("Password should be more than 6 char"))
            false
        } else {
            onSuccess()
            true
        }
    }

    fun checkConfirmPasswordField(
        confirmPassword: String,
        password: String,
        onBlank: (UiText) -> Unit,
        onUnValid: (UiText) -> Unit,
        onSuccess: () -> Unit,
    ): Boolean {
        return if (password.isBlank()) {
            onBlank(UiText.DynamicString("Please fill in this field"))
            false
        } else if (confirmPassword != password) {
            onUnValid(UiText.DynamicString("Passwords not match"))
            false
        } else {
            onSuccess()
            true
        }
    }
}