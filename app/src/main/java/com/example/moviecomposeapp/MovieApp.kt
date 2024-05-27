package com.example.moviecomposeapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.moviecomposeapp.common.constants.SeeAllType
import com.example.moviecomposeapp.navigaton.HomeSections
import com.example.moviecomposeapp.navigaton.MainDestinations
import com.example.moviecomposeapp.navigaton.MovieAppNavController
import com.example.moviecomposeapp.presentation.actor_details.ActorDetailsScreen
import com.example.moviecomposeapp.presentation.login.LoginScreen
import com.example.moviecomposeapp.presentation.movie_details.MovieDetailsScreen
import com.example.moviecomposeapp.presentation.movies.MoviesScreen
import com.example.moviecomposeapp.presentation.profile.ProfileScreen
import com.example.moviecomposeapp.presentation.search.SearchScreen
import com.example.moviecomposeapp.presentation.see_all.SeeAllScreen
import com.example.moviecomposeapp.presentation.signup.SignUpScreen
import com.example.moviecomposeapp.style.MovieAppTheme

@Composable
fun MovieApp(
    startDest: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    appNavController: MovieAppNavController,
) {

    MovieAppTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        Surface {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = appNavController.navController,
                startDestination = startDest
            ) {
                movieAppNavGraph(
                    onNavigateToRoute = appNavController::navigateToNavigationBar,
                    onCreateAccountClick = appNavController::navigateSignUp,
                    onLoginClick = appNavController::navigateToHome,
                    onSignUpClick = appNavController::navigateToHome,
                    onMovieClick = appNavController::navigateMovieDetails,
                    onSeeAllClick = appNavController::navigateSeeAll,
                    onLogOutClick = appNavController::navigateLogin,
                    onActorClick = appNavController::navigateToActorDetails,
                    upPress = appNavController::upPress,
                )
            }
        }
    }
}

private fun NavGraphBuilder.movieAppNavGraph(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    onMovieClick: (Int, NavBackStackEntry) -> Unit,
    onSeeAllClick: (SeeAllType, NavBackStackEntry) -> Unit,
    onLogOutClick: (NavBackStackEntry) -> Unit,
    onCreateAccountClick: (NavBackStackEntry) -> Unit,
    onLoginClick: (NavBackStackEntry) -> Unit,
    onSignUpClick: (NavBackStackEntry) -> Unit,
    onActorClick: (Int, NavBackStackEntry) -> Unit,
) {

    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.MOVIES.route
    ) {
        addHomeGraph(
            onNavigateToRoute = onNavigateToRoute,
            onMovieClick = onMovieClick,
            onSeeAllClick = onSeeAllClick,
            onLogOutClick = onLogOutClick,
            onActorClick = onActorClick,
            upPress = upPress,
        )
    }
    composable(route = MainDestinations.LOGIN_ROUTE) { from ->
        LoginScreen(onCreateAccountClick = remember { { onCreateAccountClick(from) } },
            onNavigationToHome = remember { { onLoginClick(from) } }
        )
    }

    composable(route = MainDestinations.SIGN_UP_ROUTE) { from ->
        SignUpScreen(onSignUpClick = remember {
            {
                onSignUpClick(from)
            }
        })
    }
}

fun NavGraphBuilder.addHomeGraph(
    onNavigateToRoute: (String) -> Unit,
    onMovieClick: (Int, NavBackStackEntry) -> Unit,
    onSeeAllClick: (SeeAllType, NavBackStackEntry) -> Unit,
    onLogOutClick: (NavBackStackEntry) -> Unit,
    onActorClick: (Int, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
) {
    composable(HomeSections.MOVIES.route) { from ->
        MoviesScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            onSeeAllClick = remember { { seeAllType -> onSeeAllClick(seeAllType, from) } },
            onNavigateToRoute = onNavigateToRoute,
        )
    }

    composable(HomeSections.SEARCH.route) { from ->
        SearchScreen(
            onNavigateToRoute = onNavigateToRoute,
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
        )
    }

    composable(HomeSections.PROFILE.route) { from ->
        ProfileScreen(
            onNavigateToRoute = onNavigateToRoute,
            onLogOutClick = remember { { onLogOutClick(from) } },
        )
    }

    composable(
        route = "${MainDestinations.SEE_ALL_ROUTE}/{${MainDestinations.SEE_ALL_TYPE_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.SEE_ALL_TYPE_KEY) { NavType.EnumType(SeeAllType::class.java) }
        )
    ) { from ->
        SeeAllScreen(
            upPress = upPress,
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
        )
    }

    composable(
        route = "${MainDestinations.MOVIE_DETAILS_ROUTE}/{${MainDestinations.MOVIE_DETAILS_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.MOVIE_DETAILS_ID_KEY) { NavType.IntType }
        )
    ) { from ->
        MovieDetailsScreen(
            upPress = upPress,
            onActorClick = remember { { actorId -> onActorClick(actorId, from) } },
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
        )
    }

    composable(
        route = "${MainDestinations.ACTOR_DETAILS_ROUTE}/{${MainDestinations.ACTOR_DETAILS_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.ACTOR_DETAILS_ID_KEY) { NavType.IntType }
        )
    ) { from ->
        ActorDetailsScreen(
            onMovieClick = remember { { movieId -> onMovieClick(movieId, from) } },
            upPress = upPress
        )
    }
}