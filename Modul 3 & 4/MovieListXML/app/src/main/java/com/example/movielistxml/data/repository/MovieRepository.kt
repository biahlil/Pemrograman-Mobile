package com.example.movielistxml.data.repository

import com.example.movielistxml.data.ResultHandler
import com.example.movielistxml.domain.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovie(postId: Int?): Flow<ResultHandler<MovieModel>>
    suspend fun getAllMovies(): Flow<ResultHandler<List<MovieModel>>>
}