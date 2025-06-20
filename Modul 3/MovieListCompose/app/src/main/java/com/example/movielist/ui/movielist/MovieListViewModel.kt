package com.example.movielist.ui.movielist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.Result
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.impl.FakeMovieRepository
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class MovieListViewModel : ViewModel() {
    val movieRepository : MovieRepository = FakeMovieRepository()

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

            movieRepository.getAllMovies().collect { result ->
                when (result) {
                    is Result.Success<*> -> {
                        _movieListState.value = MovieListState(movies = result.data)
                        Log.d("MovieListViewModel", "getAllMovies: ${_movieListState.value}")

                    }
                    is Result.Error -> {
                        _movieListState.value = MovieListState(isLoading = false)
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = result.movieError.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
        }
    }

    fun onOpenUrlClicked(url: String) {
        viewModelScope.launch {
            if (url.isNotBlank()) {
                _eventFlow.emit(UiEvent.OpenUrl(url))
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar("URL is not valid"))
            }
        }
    }
}

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class OpenUrl(val url: String) : UiEvent()
    // Anda bisa menambahkan event lain di sini, misal:
    // data class ShowSnackbar(val message: String) : UiEvent()
}