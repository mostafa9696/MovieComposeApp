package com.example.moviecomposeapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moviecomposeapp.navigaton.MainDestinations
import com.example.moviecomposeapp.navigaton.rememberAppNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val appNavController = rememberAppNavController()

            MovieApp(
                startDest = if (uiState.isUserLogin) MainDestinations.HOME_ROUTE
                else MainDestinations.LOGIN_ROUTE,
                darkTheme = uiState.isDarkModeOn,
                dynamicColor = uiState.isDynamicColorOn,
                appNavController = appNavController
            )

            if (uiState.userMessages.isNotEmpty()) {
                Toast.makeText(
                    this.applicationContext,
                    uiState.userMessages.first().asString(),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.consumedUserMessage()
            }
        }
    }
}
