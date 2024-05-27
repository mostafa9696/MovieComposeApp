package com.example.moviecomposeapp.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun Int.convertToDurationTime(): String =
    "${this / 60} H ${this % 60} M"

@Composable
fun Float.roundToDecimal(): String = String.format("%.1f", this)

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()