package com.example.movielist.data.repository

import com.example.movielist.data.Result
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovie(postId: Int?): Flow<Result<Movie>>
    fun getAllMovies(): Flow<Result<List<Movie>>>
}