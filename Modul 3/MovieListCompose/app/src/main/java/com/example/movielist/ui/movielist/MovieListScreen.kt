package com.example.movielist.ui.movielist

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.components.MovieCard
import com.example.movielist.ui.theme.MovieListTheme
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    detailOnclick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.movieListState.value
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
            // Tampilkan loading
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.movies) { movie ->
                        val imdbUrl = stringResource(movie.imdbLink)
                        MovieCard(
                            image = movie.image,
                            titleText = movie.titleText,
                            yearText = movie.yearText,
                            descriptionText = movie.descriptionText,
                            openBrowserClick = {
                                viewModel.onOpenUrlClicked(imdbUrl)
                            },
                            detailOnclick = {
                                detailOnclick(movie)
                            }
                        )
                        Timber.tag("MovieProperty").d("Movie Detail: $movie")
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun MovieListScreenPrev() {
    MovieListTheme(darkTheme = true) {
        Surface {
            MovieListScreen(viewModel = MovieListViewModel(), detailOnclick = {})
        }
    }
}

