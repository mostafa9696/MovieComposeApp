package com.example.moviecomposeapp.common.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AppDialog(
    onDismiss: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(onDismissRequest = onDismiss) {
        Card (
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp),
            shape = RoundedCornerShape(18.dp),
            content = content
        )
    }
}