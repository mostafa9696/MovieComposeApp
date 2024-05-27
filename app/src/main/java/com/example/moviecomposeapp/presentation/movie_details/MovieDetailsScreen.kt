package com.example.moviecomposeapp.presentation.movie_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviecomposeapp.common.component.ActorItem
import com.example.moviecomposeapp.common.component.AnimatedAsyncImage
import com.example.moviecomposeapp.common.component.AppScaffold
import com.example.moviecomposeapp.common.component.ErrorView
import com.example.moviecomposeapp.common.component.FullScreenCircularProgressIndicator
import com.example.moviecomposeapp.common.component.MovieItem
import com.example.moviecomposeapp.common.component.ShowUserMessage
import com.example.moviecomposeapp.common.component.TmdbLogo
import com.example.moviecomposeapp.common.component.TopAppBar
import com.example.moviecomposeapp.common.component.UserReviewItem
import com.example.moviecomposeapp.common.component.onLoadStateAppend
import com.example.moviecomposeapp.common.component.onLoadStateRefresh
import com.example.moviecomposeapp.common.constants.Dimens
import com.example.moviecomposeapp.common.constants.NetworkConstants
import com.example.moviecomposeapp.common.utils.convertToDurationTime
import com.example.moviecomposeapp.common.utils.roundToDecimal
import com.example.moviecomposeapp.models.Cast
import com.example.moviecomposeapp.models.MovieDetail
import com.example.moviecomposeapp.models.RecommendedMovieContent
import com.example.moviecomposeapp.models.Trailer
import com.example.moviecomposeapp.models.UserReviewResults
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onActorClick: (Int) -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val userReviews = uiState.userReviews.collectAsLazyPagingItems()
    val recommendations = uiState.movieRecommendations.collectAsLazyPagingItems()

    if (uiState.userMessages.isNotEmpty()) {
        ShowUserMessage(
            message = uiState.userMessages.first().asString(),
            consumedMessage = viewModel::consumedUserMessage
        )
    }

    AppScaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        when (val status = uiState.movieDataStatus) {
            is MovieDataStatus.Loading -> {
                FullScreenCircularProgressIndicator(paddingValues = paddingValues)
            }

            is MovieDataStatus.Error -> {
                ErrorView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    errorMessage = status.message.asString()
                )
            }

            is MovieDataStatus.Success -> {
                MovieDetailsScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    upPress = upPress,
                    movieDetail = uiState.movieDetails.first(),
                    castList = uiState.movieCast,
                    trailers = uiState.movieTrailers,
                    directorName = uiState.directorName,
                    onActorClick = onActorClick,
                    reviews = userReviews,
                    recommendations = recommendations,
                    onMovieClick = onMovieClick,
                    posterBackgroundColors = uiState.posterBackgroundColors,
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsScreenContent(
    modifier: Modifier,
    upPress: () -> Unit,
    movieDetail: MovieDetail,
    directorName: String,
    castList: List<Cast>,
    trailers: List<Trailer>,
    onActorClick: (Int) -> Unit,
    reviews: LazyPagingItems<UserReviewResults>,
    recommendations: LazyPagingItems<RecommendedMovieContent>,
    onMovieClick: (Int) -> Unit,
    posterBackgroundColors: List<Color>,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        MovieSection(
            upPress = upPress,
            movieDetail = movieDetail,
            directorName = directorName,
            posterBackgroundColors = posterBackgroundColors,
        )
        if (reviews.itemCount != 0) {
            UserReviewsSection(reviews = reviews)
        }
        ActorListSection(castList = castList, onActorClick = onActorClick)
        if (recommendations.itemCount != 0) {
            MovieRecommendationsSection(
                recommendations = recommendations,
                onMovieClick = onMovieClick
            )
        }
        Spacer(modifier = Modifier.height(Dimens.twoLevelPadding))
    }
}

@Composable
private fun MovieSection(
    upPress: () -> Unit,
    movieDetail: MovieDetail,
    directorName: String,
    posterBackgroundColors: List<Color>,
) {
    movieDetail.apply {
        Box(
            modifier = Modifier.drawBehind {
                drawRect(brush = Brush.verticalGradient(colors = posterBackgroundColors))
            }
        ) {
            AnimatedAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp / 1.33f)
                    .aspectRatio(2f / 3f, true),
                imageUrl = "${NetworkConstants.Paths.IMAGE_URL}${posterImageUrlPath}",
            )
            TopAppBar(
                upPress = upPress,
            )
        }
        MovieDetails(
            voteAverage = voteAverage,
            voteCount = voteCount,
            categories = genres,
            releaseDate = releaseDate,
            movieDuration = duration.convertToDurationTime(),
            directorName = directorName,
            movieName = movieName,
            overview = overview,
            movieOriginalName = originalMovieName,
        )
    }
}

@Composable
private fun MovieDetails(
    voteAverage: Float,
    voteCount: Int,
    categories: String,
    releaseDate: String,
    movieDuration: String,
    directorName: String,
    movieName: String,
    overview: String,
    movieOriginalName: String,
) {
    Column(
        modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier
                    //.conditional(condition = !isScreenWidthExpanded, ifTrue = { weight(3f) })
                    .padding(end = Dimens.twoLevelPadding),
                verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
            ) {
                Text(
                    text = if (movieName == movieOriginalName) movieName else "$movieName ($movieOriginalName)",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        value = voteAverage / 2f,
                        style = RatingBarStyle.Default,
                        onValueChange = {},
                        onRatingChanged = {},
                        size = 14.dp,
                        spaceBetween = 1.dp
                    )
                    Text(text = "${voteAverage.roundToDecimal()} ($voteCount reviews})")
                }
                Text(text = categories)
                Text(text = releaseDate)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.oneLevelPadding)) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = movieDuration,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Text(text = buildAnnotatedString {
                    append("Director: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(directorName)
                    }
                })
            }
            TmdbLogo(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
        Text(text = overview)
    }
}

@Composable
private fun ActorListSection(castList: List<Cast>, onActorClick: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)) {
        ContentSubTitle("Actors")
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            items(castList, key = { it.id }) { cast ->
                ActorItem(
                    imageUrl = "${NetworkConstants.Paths.IMAGE_URL}${cast.imageUrlPath}",
                    actorName = cast.name,
                    characterName = cast.characterName,
                    actorId = cast.id,
                    onClick = onActorClick
                )
            }
        }
    }
}


@Composable
private fun UserReviewsSection(
    reviews: LazyPagingItems<UserReviewResults>,
) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp / 2),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            contentPadding = PaddingValues(vertical = Dimens.oneLevelPadding)
        ) {
            contentStickyHeader(
                title = "Reviews",
                modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding)
            )
            items(reviews.itemCount, key = { it }) { index ->
                reviews[index]?.let { review ->
                    UserReviewItem(
                        author = review.author,
                        authorImagePath = review.authorDetails.avatarPath,
                        content = review.content,
                        createdAt = review.createdAt,
                        updatedAt = review.updatedAt,
                        rating = review.authorDetails.rating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.twoLevelPadding),
                    )
                }
            }

            reviews.loadState.apply {
                onLoadStateRefresh(loadState = refresh)
                onLoadStateAppend(loadState = append, isResultEmpty = reviews.itemCount == 0)
            }
        }
        Divider(
            modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
            thickness = 2.dp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieRecommendationsSection(
    recommendations: LazyPagingItems<RecommendedMovieContent>,
    onMovieClick: (Int) -> Unit,
) {
    val state = rememberLazyListState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    ContentSubTitle("Recommendation")
    LazyRow(
        modifier = Modifier.height(
            screenHeight / 2.5f
        ),
        contentPadding = PaddingValues(horizontal = Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    ) {
        items(recommendations.itemCount, key = { it }) { index ->
            recommendations[index]?.let { movie ->
                MovieItem(
                    id = movie.id,
                    name = movie.movieName,
                    releaseDate = movie.releaseDate,
                    imageUrl = "${NetworkConstants.Paths.IMAGE_URL}${movie.image}",
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount,
                    onClick = { onMovieClick(movie.id) },
                    modifier = Modifier.aspectRatio(2f / 3f)
                )
            }
        }
    }
}

@Composable
private fun ContentSubTitle(title: String) {
    Text(
        modifier = Modifier.padding(horizontal = Dimens.twoLevelPadding),
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.contentStickyHeader(title: String, modifier: Modifier = Modifier) {
    stickyHeader {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

