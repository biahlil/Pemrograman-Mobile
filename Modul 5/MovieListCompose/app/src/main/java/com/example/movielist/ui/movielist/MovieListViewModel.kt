package com.example.movielist.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.Result
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.movielist.UiEvent.OpenUrl
import com.example.movielist.ui.movielist.UiEvent.ShowSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // [PERBAIKAN] Menggunakan StateFlow dan sealed interface untuk UI State yang lebih jelas.
    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            repository.getAllMovies().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        Timber.tag("MovieListViewModel").d("Result: $result")
                        if (_uiState.value !is MovieListUiState.Success) {
                            _uiState.update {
                                if (result.isLoading) MovieListUiState.Loading else it
                            }
                        }
                    }
                    is Result.Error -> {
                        Timber.tag("MovieListViewModel").d("Result: $result")
                        _uiState.update { MovieListUiState.Error(result.exception.message ?: "Unknown Error") }
                        _eventFlow.emit(ShowSnackbar(result.exception.message ?: "Unknown Error"))
                    }
                    is Result.Success -> {
                        Timber.tag("MovieListViewModel").d("Result: $result")
                        Timber.tag("MovieListViewModel").d("Movies: ${result.data}")
                        _uiState.update { MovieListUiState.Success(movies = result.data) }
                    }
                }
            }
        }
    }

    fun onOpenUrlClicked(url: String) {
        viewModelScope.launch {
            if (url.isNotBlank()) {
                _eventFlow.emit(OpenUrl(url))
                Timber.tag("MovieListViewModel").i("intent into URL: $url")
            } else {
                _eventFlow.emit(ShowSnackbar("URL is not valid"))
            }
        }
    }
}

sealed interface MovieListUiState {
    data object Loading : MovieListUiState
    data class Success(val movies: List<Movie>) : MovieListUiState
    data class Error(val message: String) : MovieListUiState
}

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class OpenUrl(val url: String) : UiEvent()
}