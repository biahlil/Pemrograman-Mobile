package com.example.movielist.ui.moviedetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.Result
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.movielist.UiEvent
import com.example.movielist.ui.movielist.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _movieDetailState = mutableStateOf(MovieDetailState())
    val movieDetailState: State<MovieDetailState> = _movieDetailState

    private val _eventFlow = MutableSharedFlow<UiEvent.ShowSnackbar>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getMovieDetail(id: Int?) {
        if (id == null) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar("Movie ID tidak valid."))
            }
            return
        }
        viewModelScope.launch {
            _movieDetailState.value = MovieDetailState(isLoading = true)

            repository.getMovie(id).collect { result ->
                when (result) {
                    is  Result.Success -> {
                        _movieDetailState.value = MovieDetailState(
                            movie = result.data,
                            isLoading = false
                        )
                        Timber.tag("MovieDetailViewModel").i("Get into Movie Detail: ${_movieDetailState.value}")
                    }

                    is Result.Error -> {
                        _movieDetailState.value = MovieDetailState(isLoading = false)
                        _eventFlow.emit(
                            ShowSnackbar(
                                message = result.exception.message ?: "Movie not found"
                            )
                        )
                    }

                }
            }
        }
    }
}

data class MovieDetailState(
    val movie: Movie? = null,
    val isLoading: Boolean = true
)
