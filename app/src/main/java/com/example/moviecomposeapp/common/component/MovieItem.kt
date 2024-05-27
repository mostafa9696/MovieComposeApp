package com.example.moviecomposeapp.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moviecomposeapp.common.constants.Dimens
import com.example.moviecomposeapp.style.RatingStarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    releaseDate: String,
    imageUrl: String,
    voteAverage: Double,
    voteCount: Int,
    onClick: (Int) -> Unit,
    contentScale: ContentScale = ContentScale.Crop,
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onClick(id) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedAsyncImage(
                modifier = Modifier.fillMaxSize(),
                imageUrl = imageUrl,
                contentScale = contentScale
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind { drawRect(color = Color(0x59725959)) }
                    .padding(Dimens.twoLevelPadding).align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = releaseDate,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = RatingStarColor
                    )
                    Text(
                        text = "${String.format("%.1f", voteAverage)} ($voteCount)",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    )
                }
            }
        }
    }
}