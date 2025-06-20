package com.example.movielistxml.ui.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielistxml.data.ResultHandler
import com.example.movielistxml.data.repository.MovieRepository
import com.example.movielistxml.domain.MovieModel
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieModel>>()
    val movies: LiveData<List<MovieModel>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            movieRepository.getAllMovies().collect { movieResult ->
                when (movieResult) {
                    is ResultHandler.Success -> {
                        _movies.value = movieResult.data
                    }
                    is ResultHandler.Error -> {
                        _error.value = movieResult.exception.message
                    }
                }
                _isLoading.value = false
            }
        }
    }
}
