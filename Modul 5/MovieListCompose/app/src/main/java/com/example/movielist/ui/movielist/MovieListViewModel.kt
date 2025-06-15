package com.example.movielist.ui.movielist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.fake.FakeMovieRepository
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.launch


class MovieListViewModel : ViewModel() {
    val movieRepository : MovieRepository = FakeMovieRepository()

    private val _movies = MutableLiveData<List<Movie?>?>()
    val movies: MutableLiveData<List<Movie?>?> = _movies

    fun getAllMovies() {
        viewModelScope.launch {
            val result = movieRepository.getAllMovies()
            if (result.isSuccess) {
                _movies.value = result.data
                Log.d("MovieListViewModel", "getAllMovies: ${_movies.value}")
            }
        }
    }

}