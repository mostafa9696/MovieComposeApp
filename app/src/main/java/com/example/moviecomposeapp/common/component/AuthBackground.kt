package com.example.moviecomposeapp.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.moviecomposeapp.R

@Composable
fun AuthBackground() {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.background
        ),
        startY = sizeImage.height.toFloat() / 1.5f,
        endY = sizeImage.height.toFloat()
    )

    Box {
        Image(painter = painterResource(id = R.drawable.auth_background_image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(screenHeight)
                .width(screenWidth)
                .onGloballyPositioned { sizeImage = it.size }
                .blur(
                    radiusX = 10.dp,
                    radiusY = 10.dp
                )
        )
        Box(modifier = Modifier.matchParentSize().background(gradient))
    }
}