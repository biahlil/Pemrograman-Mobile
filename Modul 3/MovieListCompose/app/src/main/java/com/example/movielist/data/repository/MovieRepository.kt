package com.example.movielist.data.repository

import com.example.movielist.data.Result
import com.example.movielist.domain.model.Movie

interface MovieRepository {
    suspend fun getMovie(postId: Int?): Result<Movie>
    suspend fun getAllMovies(): Result<List<Movie>>
}