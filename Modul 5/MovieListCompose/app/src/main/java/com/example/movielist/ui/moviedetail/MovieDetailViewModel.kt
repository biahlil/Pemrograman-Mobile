package com.example.movielist.ui.moviedetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.fake.FakeMovieRepository
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {
    private val movieRepository: MovieRepository = FakeMovieRepository()
    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError
    fun getMovie(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            val result = movieRepository.getMovie(id)
            if (result.isSuccess) {
                _movie.value = result.data
                Log.d("MovieListViewModel", "getMovie: ${_movie.value}")
            }
        }
    }

}