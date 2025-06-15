package com.example.movielist.ui.moviedetail

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.movielist.ui.theme.MovieListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel,
    idMovie: Int,
    onBack: () -> Unit = {}
) {
    val movie by viewModel.movie.observeAsState(null)
    LaunchedEffect(Unit) {
        viewModel.getMovie(idMovie)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(movie?.titleText ?: R.string.kiki_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                MovieCover(cover = movie?.image ?: R.drawable.kiki_cover)
            }
            item {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(movie?.titleText ?: R.string.kiki_title),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 3,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = stringResource(movie?.yearText ?: R.string.kiki_year),
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
                        text = stringResource(movie?.descriptionText ?: R.string.kiki_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

}

@Composable
fun MovieCover(
    @DrawableRes cover: Int
) {
    // 1) Grab the current configuration
    val configuration = LocalConfiguration.current

    // 2) Build a different Modifier depending on orientation
    val imageModifier = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // In landscape: keep a 9:16 (w:h) ratio
        Modifier
            .padding(10.dp)
            .heightIn(min = 100.dp, max = 300.dp)
            .fillMaxWidth()
    } else {
        // In portrait: cap height at 500.dp (and at least 200.dp)
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun MovieDetailScreenPrev() {
    MovieListTheme(
        darkTheme = true
    ) {
        Surface {
            MovieDetailScreen(
                viewModel = MovieDetailViewModel(),
                idMovie = 1
            )
        }
    }
}