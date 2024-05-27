package com.example.moviecomposeapp.presentation.actor_details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviecomposeapp.common.component.AnimatedAsyncImage
import com.example.moviecomposeapp.common.component.AppScaffold
import com.example.moviecomposeapp.common.component.ErrorView
import com.example.moviecomposeapp.common.component.FullScreenCircularProgressIndicator
import com.example.moviecomposeapp.common.component.MovieItem
import com.example.moviecomposeapp.common.constants.Dimens
import com.example.moviecomposeapp.common.constants.NetworkConstants
import com.example.moviecomposeapp.common.helpers.UiState
import com.example.moviecomposeapp.models.ActorDetails
import com.example.moviecomposeapp.models.ActorMoviesContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorDetailsScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: ActorDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = uiState.actorName)
                },
                navigationIcon = {
                    IconButton(onClick = upPress) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        ActorDetailsScreenContent(
            modifier = Modifier.padding(paddingValues),
            actorDetailsState = uiState.actorDetailsState,
            actorMoviesState = uiState.actorMoviesState,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun ActorDetailsScreenContent(
    modifier: Modifier,
    actorDetailsState: UiState<ActorDetails>,
    actorMoviesState: UiState<List<ActorMoviesContent>>,
    onMovieClick: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActorDetailsSection(actorDetailsState = actorDetailsState)
        ActorMoviesSection(
            actorMoviesState = actorMoviesState,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun ActorDetailsSection(actorDetailsState: UiState<ActorDetails>) {
    Log.d("vv8", "ActorDetailsSection: $actorDetailsState")
    when (actorDetailsState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator()
        }

        is UiState.OnDataLoaded -> {
            actorDetailsState.data.apply {
                AnimatedAsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(LocalConfiguration.current.screenWidthDp.dp / 2),
                    imageUrl = "${NetworkConstants.Paths.IMAGE_URL}${profileImagePath}",
                    borderShape = CircleShape,
                    borderStroke = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                ContentTextRow("Place of birth", contentText = placeOfBirth)
                if (deathDay != null) {
                    ContentTextRow(
                        subtitle = "Birth and death days",
                        contentText = "$birthday / $deathDay"
                    )
                } else {
                    ContentTextRow("Birthday", contentText = birthday)
                }
                if (biography.isNotBlank()) {
                    ContentTextRow("Biography", contentText = biography)
                }
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = Modifier.fillMaxWidth(),
                errorMessage = actorDetailsState.errorMessage.toString()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ActorMoviesSection(
    actorMoviesState: UiState<List<ActorMoviesContent>>,
    onMovieClick: (Int) -> Unit,
) {
    when (actorMoviesState) {
        is UiState.Loading -> {
            FullScreenCircularProgressIndicator()
        }

        is UiState.OnDataLoaded -> {
            val state = rememberLazyListState()

            val screenHeight = LocalConfiguration.current.screenHeightDp.dp

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimens.oneLevelPadding,
                        start = Dimens.twoLevelPadding
                    ),
                text = "Actor Movies",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            LazyRow(
                modifier = Modifier.height(
                    screenHeight / 2.5f
                ),
                contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
                horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
            ) {
                items(actorMoviesState.data, key = { movie -> movie.id }) { movie ->
                    MovieItem(
                        id = movie.id,
                        name = movie.movieName,
                        releaseDate = movie.releaseDate,
                        imageUrl = "${NetworkConstants.Paths.IMAGE_URL}/${movie.posterImagePath}",
                        voteAverage = movie.voteAverage,
                        voteCount = movie.voteCount,
                        onClick = onMovieClick,
                        modifier = Modifier.aspectRatio(2f / 3f)
                    )
                }
            }
        }

        is UiState.OnError -> {
            ErrorView(
                modifier = Modifier.fillMaxWidth(),
                errorMessage = actorMoviesState.errorMessage.toString()
            )
        }
    }
}

@Composable
private fun ContentTextRow(subtitle: String, contentText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(subtitle)
            }
            append(contentText)
        })
    }
}