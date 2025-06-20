package com.example.movielist.ui.moviedetail

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielist.R
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.theme.MovieListTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel,
    movieId: Int?,
    navigateUp: () -> Unit
) {
    val state = viewModel.movieDetailState.value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(movieId) {
        viewModel.getMovieDetail(movieId)
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                else -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(state.movie?.titleText ?: R.string.loading)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                MovieDetailContent(
                    movie = state.movie ?: Movie(
                        0,
                        R.drawable.venom_cover,
                        R.string.venom_title,
                        R.string.venom_year,
                        R.string.venom_detail,
                        R.string.venom_imdb
                    ),
                    modifier = modifier
                )
            }
        }

    }

}
@Composable
fun MovieCover(
    @DrawableRes cover: Int
) {
    val configuration = LocalConfiguration.current
    val imageModifier = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Modifier
            .padding(10.dp)
            .heightIn(min = 100.dp, max = 300.dp)
            .fillMaxWidth()
    } else {
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 500.dp)
    }
    Image(
        painter = painterResource(cover),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = imageModifier
    )
}

@Composable
fun MovieDetailContent(movie: Movie, modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            MovieCover(cover = movie.image)
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(movie.titleText),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 3,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = stringResource(movie.yearText),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.plot),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(movie.descriptionText),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun MovieDetailScreenPrev() {
    MovieListTheme(
        darkTheme = true
    ) {
        Surface {
        }
    }
}
