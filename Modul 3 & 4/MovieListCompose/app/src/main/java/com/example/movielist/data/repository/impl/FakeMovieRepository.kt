package com.example.movielist.data.repository.impl

import com.example.movielist.data.Result
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.IOException

class FakeMovieRepository : MovieRepository {

    private val movies = listOf(venom, kiki, dayLater, turningRed, vhs, castle)

    override fun getMovie(postId: Int?): Flow<Result<Movie>> {
        return flow {
            delay(1000)

            if (postId == null) {
                emit(Result.Error(Exception("Movie ID can't be null")))
                return@flow // Hentikan eksekusi flow
            }

            val movieById: Movie? = movies.find { it.id == postId }

            if (movieById != null) {
                emit(Result.Success(movieById))
            } else {
                emit(Result.Error(Exception("Movie with id $postId not found")))
            }
        }
    }

    override suspend fun getAllMovies(): Flow<Result<List<Movie>>> {
        return flow {
            try {
                delay(1000)
                emit(Result.Success(movies))
            } catch (e: IOException) {
                emit(Result.Error(Exception(e.message)))
            }
        }
    }
}