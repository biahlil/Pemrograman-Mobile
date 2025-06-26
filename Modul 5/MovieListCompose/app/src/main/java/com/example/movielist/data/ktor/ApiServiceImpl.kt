package com.example.movielist.data.ktor

import com.example.movielist.data.ktor.dto.MovieDetailDto
import com.example.movielist.data.ktor.dto.MovieListResponseDto

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import timber.log.Timber

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getDiscoverMovies(): MovieListResponseDto {
        return try {

            client.get("/3/discover/movie") {
                url {
                    parameters.append("include_adult", "false")
                    parameters.append("include_video", "false")
                    parameters.append("language", "en-US")
                    parameters.append("sort_by", "popularity.desc")
                    parameters.append("include_video", "false")
                    parameters.append("with_genres", "16")
                }
                accept(ContentType.Application.Json)
                Timber.tag("ApiService").d("Berhasil mengambil daftar film")
            }.body()

        } catch (e: Exception) {
            Timber.tag("ApiService").e(e, "Error saat mengambil daftar film")
            MovieListResponseDto(emptyList())
        }
    }


    override suspend fun getMovieDetail(id: Int): MovieDetailDto? {
        return try {
            client.get("/3/movie/$id") {
                url {
                    parameters.append("language", "en-US")
                }
                accept(ContentType.Application.Json)
            }.body()
        } catch (e: ClientRequestException) {
            // 4xx errors
            Timber.tag("ApiService").e(e, "ClientRequestException")
            null
        } catch (e: Exception) {
            Timber.tag("ApiService").e(e, "Error saat mengambil detail film")
            null
        }
    }
}