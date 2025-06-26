package com.example.movielist.ui.movielist

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.components.MovieCard
import com.example.movielist.ui.theme.MovieListTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun MovieListScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = hiltViewModel(),
    detailOnclick: (Movie) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is UiEvent.OpenUrl -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, event.url.toUri())
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Timber.tag("MovieListScreen")
                            .e("Error opening URL: ${e.message} ${event.url}")
                    }
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is MovieListUiState.Loading -> {
                    Timber.tag("MovieListScreen").d("Loading...$state")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MovieListUiState.Error -> {
                    Timber.tag("MovieListScreen").e("Error: ${state.message}")
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                is MovieListUiState.Success -> {
                    Timber.tag("MovieListScreen").d("Movies: ${state.movies}")
                    MovieListContent(
                        movies = state.movies,
                        onImdbClick = { imdbUrl ->
                            viewModel.onOpenUrlClicked(imdbUrl)
                        },
                        onDetailClick = detailOnclick
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListContent(
    movies: List<Movie>,
    onImdbClick: (String) -> Unit,
    onDetailClick: (Movie) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieCard(
                image = movie.imageUrl,
                titleText = movie.title,
                yearText = movie.year,
                descriptionText = movie.description,
                openBrowserClick = {
                    // [FIX] Memanggil fungsi dengan parameter yang benar
                    onImdbClick(movie.imdbURL.toString())
                },
                detailOnclick = {
                    // [FIX] Memanggil fungsi dengan parameter yang benar
                    onDetailClick(movie)
                }
            )
            Timber.tag("MovieProperty").d("Movie Detail: $movie")
        }
    }
}

@Preview(showBackground = true, name = "Movie List - Success")
@Composable
private fun MovieListSuccessPreview() {
    val fakeMovies = listOf(
        Movie("1", "", "Contoh Film 1", "2023", "Ini adalah deskripsi untuk film pertama.", "imdb.com"),
        Movie("2", "", "Contoh Film 2", "2024", "Ini adalah deskripsi untuk film kedua yang sedikit lebih panjang dari yang pertama.", "imdb.com")
    )
    MovieListTheme {
        MovieListContent(
            movies = fakeMovies,
            onImdbClick = {},
            onDetailClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Movie List - Loading")
@Composable
private fun MovieListLoadingPreview() {
    MovieListTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
