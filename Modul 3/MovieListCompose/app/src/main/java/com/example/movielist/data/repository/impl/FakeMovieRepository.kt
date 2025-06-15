package com.example.movielist.data.repository.impl

import com.example.movielist.data.Result
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.delay

class FakeMovieRepository : MovieRepository {

    private val movies = listOf(venom, kiki, dayLater, turningRed, vhs, castle)

    override suspend fun getMovie(postId: Int?): Result<Movie> {
        val movieById: Movie? = movies.find { it.id == postId }
        delay(200)
        return movieById
            .takeIf { it != null }
            ?.let { Result.Success(it) }
            ?: Result.Error(Exception("Movie not found"))
    }

    override suspend fun getAllMovies(): Result<List<Movie>> {
        delay(200)
        return movies
            .takeIf { it.isNotEmpty() }
            ?.let { Result.Success(it) }
            ?: Result.Error(Exception("Movies not found"))
    }
}