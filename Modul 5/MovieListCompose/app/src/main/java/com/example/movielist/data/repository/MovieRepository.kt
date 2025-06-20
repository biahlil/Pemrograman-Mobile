package com.example.movielist.data.repository

import com.example.movielist.data.ResultOrError
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovie(postId: Int?): ResultOrError<Movie>
    suspend fun getAllMovies(): ResultOrError<List<Movie>>
    suspend fun getRandomMovie(): Flow<ResultOrError<List<Movie>>>
}