package com.example.searchmovieapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.searchmovieapp.features.home.Home
import com.example.searchmovieapp.features.home.HomeScreen
import com.example.searchmovieapp.ui.components.CustomSnackBar
import com.example.searchmovieapp.ui.theme.SearchMovieAppTheme
import kotlinx.coroutines.launch

@Composable
fun SearchMovieApp() {

    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    SearchMovieAppTheme {

        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    snackbar = { CustomSnackBar(data = it, maxLines = 2) }
                )
            }
        ) { paddingValues ->
            AppNavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController
            ) { message ->
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Long,
                        withDismissAction = true
                    )
                }
            }
        }

    }
}

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    onSnackBar: (message: String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(onSnackBar = onSnackBar)
        }
    }
}