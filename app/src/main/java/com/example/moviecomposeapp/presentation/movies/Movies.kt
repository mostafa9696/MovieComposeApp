package com.example.moviecomposeapp.presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviecomposeapp.common.component.AppScaffold
import com.example.moviecomposeapp.common.component.ErrorView
import com.example.moviecomposeapp.common.component.FullScreenCircularProgressIndicator
import com.example.moviecomposeapp.common.component.MovieButton
import com.example.moviecomposeapp.common.component.MovieItem
import com.example.moviecomposeapp.common.constants.Dimens
import com.example.moviecomposeapp.common.constants.NetworkConstants
import com.example.moviecomposeapp.common.constants.SeeAllType
import com.example.moviecomposeapp.models.MovieContent
import com.example.moviecomposeapp.navigaton.HomeSections
import com.example.moviecomposeapp.ui.MovieNavigationBar

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MovieNavigationBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.MOVIES.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        when (val status = uiState.movieDataStatus) {
            is MovieDataStatus.Loading -> FullScreenCircularProgressIndicator(paddingValues = paddingValues)
            is MovieDataStatus.Error -> ErrorView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                errorMessage = status.message.asString()
            )

            is MovieDataStatus.Success -> {
                MoviesScreenContent(
                    modifier = Modifier
                        .padding(paddingValues),
                    onMovieClick = onMovieClick,
                    onSeeAllClick = onSeeAllClick,
                    trendingMoviesList = uiState.trendingMovies,
                    topRatedMoviesList = uiState.topRatedMovies,
                    upcomingMoviesList = uiState.upcomingMovies
                )
            }
        }
    }
}

@Composable
private fun MoviesScreenContent(
    modifier: Modifier,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (SeeAllType) -> Unit,
    trendingMoviesList: List<MovieContent>,
    topRatedMoviesList: List<MovieContent>,
    upcomingMoviesList: List<MovieContent>,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Dimens.twoLevelPadding),
        verticalArrangement = Arrangement.spacedBy(Dimens.threeLevelPadding)
    ) {
        MovieSection(
            sectionHeight = screenHeight / 2,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            seeAllType = SeeAllType.TRENDING,
            title = "Trending",
            movieList = trendingMoviesList
        )
        MovieSection(
            sectionHeight = screenHeight / 2.5f,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            seeAllType = SeeAllType.TOP_RATED,
            title = "Top Rated",
            movieList = topRatedMoviesList
        )
        MovieSection(
            sectionHeight = screenHeight / 2.5f,
            onSeeAllClick = onSeeAllClick,
            onMovieClick = onMovieClick,
            seeAllType = SeeAllType.UPCOMING,
            title = "Upcoming",
            movieList = upcomingMoviesList
        )

    }
}

@Composable
private fun MovieSection(
    sectionHeight: Dp,
    movieImageRatio: Float = 2f / 3f,
    onSeeAllClick: (SeeAllType) -> Unit,
    onMovieClick: (Int) -> Unit,
    movieList: List<MovieContent>,
    seeAllType: SeeAllType,
    title: String,
) {
    Column(
        modifier = Modifier.height(sectionHeight),
        verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
    ) {
        ContentTitleSection(
            text = title,
            onSeeAllClick = onSeeAllClick,
            type = seeAllType
        )

        val lazyState = rememberLazyListState()

        LazyRow(
            modifier = Modifier.padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
            state = lazyState,
            //flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyState)
        ) {
            items(
                movieList,
                key = { movie -> movie.id }
            ) { movie ->
                MovieItem(
                    modifier = Modifier.aspectRatio(movieImageRatio),
                    id = movie.id,
                    name = movie.movieName,
                    releaseDate = movie.releaseDate,
                    imageUrl = "${NetworkConstants.Paths.IMAGE_URL}${movie.posterImagePath}",
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount ?: 0,
                    onClick = onMovieClick
                )
            }
        }
    }

}


@Composable
private fun ContentTitleSection(
    text: String,
    onSeeAllClick: (SeeAllType) -> Unit,
    type: SeeAllType,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.twoLevelPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        MovieButton(
            text = "See all",
            onClick = { onSeeAllClick(type) },
            fontSize = 16.sp
        )
    }
}