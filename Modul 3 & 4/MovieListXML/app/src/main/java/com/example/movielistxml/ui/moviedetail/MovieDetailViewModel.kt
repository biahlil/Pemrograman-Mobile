package com.example.movielistxml.ui.moviedetail

import com.example.movielistxml.data.repository.MovieRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielistxml.data.ResultHandler
import com.example.movielistxml.domain.MovieModel
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movie = MutableLiveData<MovieModel?>()
    val movie: LiveData<MovieModel?> = _movie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()

    fun getMovieDetail(movieId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            _isLoading.value = true
            movieRepository.getMovie(movieId).collect { movieResult ->
                when (movieResult) {
                    is ResultHandler.Success -> {
                        _movie.value = movieResult.data
                    }
                    is ResultHandler.Error -> {
                        _movie.value = null
                        _error.value = movieResult.exception.message
                    }
                }
                _isLoading.value = false
            }
        }
    }
}
