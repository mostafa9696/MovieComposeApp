package com.example.moviecomposeapp.common.component

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AuthWelcomeMessage(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.wrapContentSize(),
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.W500),
    )
}