package com.example.movielist.ui.moviedetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.Result
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.movielist.UiEvent
import com.example.movielist.ui.movielist.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        // Ambil movieId dari argumen yang dikirim oleh NavHost
        val movieId: String? = savedStateHandle["movieId"]

        movieId?.toIntOrNull()?.let { id ->
            repository.getMovie(id)
        } ?: run {
            _uiState.value = MovieDetailUiState.Error("ID Film tidak valid.")
        }
    }

    fun getMovieDetail(id: Int?) {
        viewModelScope.launch {

            repository.getMovie(id).collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> MovieDetailUiState.Loading
                    is Result.Success -> {
                        MovieDetailUiState.Success(result.data)
                    }
                    is Result.Error -> {
                        MovieDetailUiState.Error(result.exception.message ?: "Terjadi error yang tidak diketahui")
                    }
                }
            }
        }
    }
}

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movie: Movie) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}