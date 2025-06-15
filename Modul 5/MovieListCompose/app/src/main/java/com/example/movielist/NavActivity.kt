package com.example.movielist

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movielist.NavRoutes.MOVIE_DETAIL_WITH_ARG
import com.example.movielist.ui.moviedetail.MovieDetailScreen
import com.example.movielist.ui.moviedetail.MovieDetailViewModel
import com.example.movielist.ui.movielist.MovieListScreen
import com.example.movielist.ui.movielist.MovieListViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.MOVIE_LIST,
        modifier = modifier
    ) {
        // Movie List Destinations
        composable(NavRoutes.MOVIE_LIST) {
            MovieListScreen(
                viewModel = MovieListViewModel(),
                imdbOnclick = {},
                detailOnclick = {
                    val route = "${NavRoutes.MOVIE_DETAIL_BASE}/${it.id}"
                    navController.navigate(route)
                },
                modifier = modifier
            )
        }

        // Movie Detail Destinations
        composable(
            route = NavRoutes.MOVIE_DETAIL_WITH_ARG,
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (movieId != null) {
                MovieDetailScreen(
                    viewModel = MovieDetailViewModel(),
                    idMovie = movieId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}