package com.example.movielistxml.data.repository.impl

import com.example.movielistxml.data.ResultHandler
import com.example.movielistxml.data.repository.MovieRepository
import com.example.movielistxml.domain.MovieModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeMovieRepository : MovieRepository {

    private val movies = listOf(venom, kiki, dayLater, turningRed, vhs, castle)

    override fun getMovie(postId: Int?): Flow<ResultHandler<MovieModel>> {
        return flow {
            delay(1000)

            if (postId == null) {
                emit(ResultHandler.Error(Exception("MovieModel ID can't be null")))
                return@flow // Hentikan eksekusi flow
            }

            val movieById: MovieModel? = movies.find { it.id == postId }

            if (movieById != null) {
                emit(ResultHandler.Success(movieById))
            } else {
                emit(ResultHandler.Error(Exception("MovieModel with id $postId not found")))
            }
        }
    }

    override suspend fun getAllMovies(): Flow<ResultHandler<List<MovieModel>>> {
        return flow {
            try {
                delay(1000)
                emit(ResultHandler.Success(movies))
            } catch (e: IOException) {
                emit(ResultHandler.Error(Exception(e.message)))
            }
        }
    }
}