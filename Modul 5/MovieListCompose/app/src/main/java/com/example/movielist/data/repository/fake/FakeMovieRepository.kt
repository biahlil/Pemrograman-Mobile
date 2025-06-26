package com.example.movielist.data.repository.fake

import android.content.Context
import com.example.movielist.data.Result
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.domain.model.FakeMovie
import com.example.movielist.domain.model.Movie
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieRepository(private val context: Context) : MovieRepository {

    private val fakeMovies = listOf(venom, kiki, dayLater, turningRed, vhs, castle)

    // Fungsi untuk mengubah (map) FakeMovie menjadi Movie
    private fun FakeMovie.toMovie(): Movie {
        // Bentuk URI untuk drawable resource. Coil dapat membacanya.
        val imageUri = "android.resource://${context.packageName}/$image"

        return Movie(
            id = id.toString(),
            imageUrl = imageUri,
            title = context.getString(titleText),
            year = context.getString(yearText),
            description = context.getString(descriptionText),
            imdbURL = context.getString(imdbLink)
        )
    }

    override fun getMovie(postId: Int?): Flow<Result<Movie>> {
        return flow {
            delay(1000)

            if (postId == null) {
                emit(Result.Error(Exception("Movie ID tidak bisa null")))
                return@flow // Hentikan eksekusi flow
            }

            val fakeMovieById = fakeMovies.find { it.id == postId }

            if (fakeMovieById != null) {
                emit(Result.Success(fakeMovieById.toMovie()))
            } else {
                emit(Result.Error(Exception("Movie dengan id $fakeMovieById tidak ditemukan")))
            }
        }
    }

    override fun getAllMovies(): Flow<Result<List<Movie>>> {
        return flow {
            try {
                delay(1000)
                val movies = fakeMovies.map { it.toMovie() }
                emit(Result.Success(movies))
            } catch (e: IOException) {
                emit(Result.Error(Exception(e.message)))
            }
        }
    }
}