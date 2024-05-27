package com.example.moviecomposeapp.navigaton

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

object MainDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signUp"
    const val MOVIE_DETAILS_ROUTE = "movieDetails"
    const val MOVIE_DETAILS_ID_KEY = "movieId"
    const val SEE_ALL_ROUTE = "seeAll"
    const val SEE_ALL_TYPE_KEY = "seeAllType"
    const val HOME_ROUTE = "home"
    const val ACTOR_DETAILS_ROUTE = "actorDetails"
    const val ACTOR_DETAILS_ID_KEY = "actorId"
}

enum class HomeSections(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    MOVIES("Movies", Icons.Filled.Movie, Icons.Outlined.Movie, "home/movies"),
    SEARCH("Search", Icons.Filled.Search, Icons.Outlined.Search, "home/search"),
    PROFILE(
        "Profile",
        Icons.Filled.AccountCircle,
        Icons.Outlined.AccountCircle,
        "home/profile"
    )
}