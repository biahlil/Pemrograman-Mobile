package com.example.movielist.data.repository.network


import com.example.movielist.data.ResultOrError
import com.example.movielist.data.ktor.ApiService
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.Movie
import com.example.movielist.domain.model.MovieError
import io.ktor.client.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Concrete implementation of MovieRepository (domain) using ApiService under the hood.
 * We wrap any exceptions into domain‑level MovieError and emit a ResultOrError.
 */
class MovieRepositoryImpl(
    private val apiService: ApiService
) : MovieRepository {

    /** Helper to translate low‑level exceptions → MovieError */
    private fun mapExceptionToMovieError(e: Throwable): MovieError =
        when (e) {
            is ClientRequestException, is ServerResponseException -> MovieError.NotFound
            is HttpRequestTimeoutException, is java.io.IOException -> MovieError.Network
            else -> MovieError.Unknown
        }

    override suspend fun getMovie(postId: Int?): ResultOrError<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMovies(): ResultOrError<List<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRandomMovie(): Flow<ResultOrError<List<Movie>>> = flow {
        try {
            val dtoList = apiService.fetchAllMovies()              // may throw
            val domainList = dtoList.map {
                val detailDto = apiService.fetchMovieImdbById(it.id.toString())
                it.toDomain()

            }
            emit(ResultOrError(data = domainList))
        } catch (e: Throwable) {
            val err = mapExceptionToMovieError(e)
            emit(ResultOrError(error = err))
        }
    }.flowOn(Dispatchers.IO)
}

//
//val detailDto = apiService.fetchMovieImdbById(id)
//val domainMovie = baseMovie.copy(imdbId = detailDto?.imdbId)
//override suspend fun getMovieById(id: Int): ResultOrError<Movie> {
//    // first fetch the base MovieDto → domain Movie
//    val base = apiService.fetchMovieDto(id)?.toDomain() ?: return ResultOrError(error = MovieError.NotFound)
//    // then fetch the detail JSON
//    val detailDto = apiService.fetchMovieDetail(id)
//    // merge the ID into the domain model
//    val enriched = detailDto?.toDomain(base) ?: base
//    return ResultOrError(data = enriched)
//}
