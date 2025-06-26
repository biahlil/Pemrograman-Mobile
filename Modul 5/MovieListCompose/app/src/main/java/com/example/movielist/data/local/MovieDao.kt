package com.example.movielist.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Flow<MovieEntity?>

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAll()

    @Query("UPDATE movies SET imdbId = :imdbId WHERE id = :movieId")
    suspend fun updateImdbId(movieId: Int, imdbId: String)
}