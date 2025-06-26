package com.example.movielist.ui.moviedetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.fake.FakeMovieRepository
import com.example.movielist.data.Result
import com.example.movielist.domain.model.Movie
import com.example.movielist.ui.movielist.UiEvent
import com.example.movielist.ui.movielist.UiEvent.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

//class MovieDetailViewModel : ViewModel() {
//    private val movieRepository: MovieRepository = FakeMovieRepository()
//    private val _movie = MutableLiveData<Movie>()
//    val movie: LiveData<Movie> = _movie
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//    private val _isError = MutableLiveData<Boolean>()
//    val isError: LiveData<Boolean> = _isError
//    fun getMovie(id: Int) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _isError.value = false
//            val result = movieRepository.getMovie(id)
//            if (result.isSuccess) {
//                _movie.value = result.data
//                Log.d("MovieListViewModel", "getMovie: ${_movie.value}")
//            }
//        }
//    }
//
//}




class MovieDetailViewModel(
    private val movieRepository: MovieRepository = FakeMovieRepository(),
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

            movieRepository.getMovie(id).collect { result ->
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
