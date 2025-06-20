package com.example.movielist.ui.movielist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielist.R
import com.example.movielist.ui.components.MovieCard
import com.example.movielist.ui.theme.MovieListTheme
import androidx.compose.runtime.getValue
import com.example.movielist.domain.model.Movie

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    imdbOnclick: (Movie) -> Unit,
    detailOnclick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val movieList by viewModel.movies.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        viewModel.getAllMovies()
    }
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        items(movieList) { movie ->
            MovieCard(
                image = movie.image,
                titleText = movie.titleText,
                yearText = movie.yearText,
                descriptionText = movie.descriptionText,
                url = movie.imdbLink,
                detailOnclick = { detailOnclick(movie) },
                modifier = Modifier.fillMaxWidth()
            )
            Log.d("Movie Property", "${movie.id}")
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun MovieListScreenPrev() {
    MovieListTheme(darkTheme = true) {
        Surface {
            MovieListScreen(viewModel = MovieListViewModel(), imdbOnclick = {}, detailOnclick = {})
        }
    }
}

