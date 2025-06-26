package com.example.movielist.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.movielist.data.Result
import com.example.movielist.ui.moviedetail.MovieDetailUiState
import com.example.movielist.ui.movielist.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _movieListState = mutableStateOf(MovieListState())
    val movieListState: State<MovieListState> = _movieListState

    init {
        getAllMovies()
    }

    fun getAllMovies() {
        viewModelScope.launch {
            _movieListState.value = MovieListState(isLoading = true)

            repository.getAllMovies().collect { result ->
                when (result) {
                    is Result.Success<List<Movie>> -> {
                        _movieListState.value = MovieListState(movies = result.data, isLoading = false)
                        Timber.tag("MovieListViewModel").i("Movies: ${_movieListState.value}")

                    }
                    is Result.Error -> {
                        _movieListState.value = MovieListState(isLoading = false)
                        _eventFlow.emit(
                            ShowSnackbar(
                                message = result.exception.message ?: "Unknown Error"
                            )
                        )
                    }
                    is Result.Loading -> MovieDetailUiState.Loading

                }
            }
        }
    }

    fun onOpenUrlClicked(url: String) {
        viewModelScope.launch {
            if (url.isNotBlank()) {
                _eventFlow.emit(UiEvent.OpenUrl(url))
                Timber.tag("MovieListViewModel").i("intent into URL: $url")
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar("URL is not valid"))
            }
        }
    }
}

data class MovieListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = true
)

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class OpenUrl(val url: String) : UiEvent()
}