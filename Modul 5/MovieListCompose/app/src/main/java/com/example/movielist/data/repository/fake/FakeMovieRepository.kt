package com.example.movielist.data.repository.fake

import com.example.movielist.data.ResultOrError
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.Movie
import com.example.movielist.domain.model.MovieError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class FakeMovieRepository : MovieRepository {

    private val movies = listOf(venom, kiki, dayLater, turningRed, vhs, castle)

    override suspend fun getMovie(postId: Int?): ResultOrError<Movie> {
        val movieById: Movie? = movies.find { it.id == postId.toString() }
        delay(200)
        return movieById
            .takeIf { it != null }
            ?.let { ResultOrError(data = it) }
            ?: ResultOrError(error = MovieError.NotFound)
    }

    override suspend fun getAllMovies(): ResultOrError<List<Movie>> {
        delay(200)
        return movies
            .takeIf { it.isNotEmpty() }
            ?.let { ResultOrError(data = it) }
            ?: ResultOrError(error = MovieError.NotFound)
    }

    override suspend fun getRandomMovie(): Flow<ResultOrError<List<Movie>>> {
        TODO("Not yet implemented")
                }
}
