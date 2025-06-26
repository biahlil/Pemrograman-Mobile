package com.example.movielist.data.repository.network

import com.example.movielist.data.Result
import com.example.movielist.data.ktor.ApiService
import com.example.movielist.data.local.MovieDao
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.toDomainMovie
import com.example.movielist.data.toEntity
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.awaitAll
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl (
    private val api: ApiService,
    private val dao: MovieDao
) : MovieRepository {

    override fun getAllMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading(true))

        val cachedMovies = dao.getAllMovies().map { entities ->
            entities.map { it.toDomainMovie() }
        }

        var isCacheEmitted = false
        cachedMovies.collect { movies ->
            if (movies.isNotEmpty()) {
                isCacheEmitted = true
                emit(Result.Success(movies))
            }
        }

        try {
            val initialMovies = api.getDiscoverMovies().listMovie
            val detailedMovies = coroutineScope {
                initialMovies.map { movieDto ->
                    async {
                        api.getMovieDetail(movieDto.id)
                    }
                }.awaitAll()
            }

            val movieEntities = detailedMovies.mapNotNull { detailDto ->
                detailDto?.toEntity()
            }

            if (movieEntities.isNotEmpty()) {
                dao.clearAll()
                dao.insertAll(movieEntities)
            }
        } catch (e: Exception) {
            if (!isCacheEmitted) {
                emit(Result.Error(e))
            }
        }
    }

    // Fungsi getMovie detail
    override fun getMovie(postId: Int?): Flow<Result<Movie>> {
        return flow {
            emit(Result.Loading(true))
            if (postId == null) {
                emit(Result.Error(Exception("Movie ID tidak valid.")))
                return@flow
            }

            try {
                val remoteMovieDto = api.getMovieDetail(postId)
                if (remoteMovieDto != null) {
                    val domainMovie = remoteMovieDto.toEntity().toDomainMovie()
                    emit(Result.Success(domainMovie))
                } else {
                    emit(Result.Error(Exception("Film dengan ID $postId tidak ditemukan.")))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }
}