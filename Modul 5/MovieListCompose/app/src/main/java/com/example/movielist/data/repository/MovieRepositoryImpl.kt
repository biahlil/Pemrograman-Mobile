package com.example.movielist.data.repository

import com.example.movielist.data.Result
import com.example.movielist.data.ktor.ApiService
import com.example.movielist.data.local.MovieDao
import com.example.movielist.data.toDomainMovie
import com.example.movielist.data.toEntity
import com.example.movielist.domain.model.Movie
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl (
    private val api: ApiService,
    private val dao: MovieDao
) : MovieRepository {

    override fun getAllMovies(): Flow<Result<List<Movie>>> = channelFlow {
        send(Result.Loading(true))

        try {
            val remoteMovies = api.getDiscoverMovies().results
            val movieEntities = remoteMovies.map { it.toEntity() }
            Timber.Forest.tag("MovieRepositoryImpl").d("getDiscoverMovies: $remoteMovies")
            dao.clearAll()
            dao.insertAll(movieEntities)
            Timber.Forest.tag("MovieRepositoryImpl").d("insertAll: $movieEntities")

            coroutineScope {
                movieEntities.forEach { movie ->
                    launch {
                        val detail = api.getMovieDetail(movie.id)
                        Timber.Forest.tag("MovieRepositoryImpl").d("getMovieDetail: $detail")
                        if (detail?.imdbId != null) {
                            dao.updateImdbId(movie.id, detail.imdbId)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(Result.Error(e))
        } finally {
            send(Result.Loading(false))
        }
        dao.getAllMovies().collect { movies ->
            Timber.Forest.tag("MovieRepositoryImpl").d("getAllMovies: $movies")
            send(Result.Success(movies.map { it.toDomainMovie() }))
        }
    }

    override fun getMovie(postId: Int?): Flow<Result<Movie>> {
        return flow {
            emit(Result.Loading(true))
            if (postId == null) {
                emit(Result.Error(Exception("Movie ID tidak valid.")))
                return@flow
            }
            try {
                dao.getMovieById(postId).collect { entity ->
                    if (entity != null) {
                        emit(Result.Success(entity.toDomainMovie()))
                    } else {
                        emit(Result.Error(Exception("Film dengan ID $postId tidak ditemukan di cache.")))
                    }
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            } finally {
                emit(Result.Loading(false))

            }
        }
    }
}