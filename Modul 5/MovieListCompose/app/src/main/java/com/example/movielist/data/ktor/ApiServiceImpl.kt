package com.example.movielist.data.ktor

import com.example.movielist.data.ktor.dto.MovieDetailDto
import com.example.movielist.data.ktor.dto.MovieDto

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerializationException

/**
 * Ktor‑based implementation of ApiService.
 * Adjust baseUrl to your real endpoint. Here we simulate “https://api.example.com”.
 */
class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun fetchAllMovies(): List<MovieDto> {
        return try {
            client.get("/discover/movie") {
                url{
                    parameters.append("include_adult", "false")
                    parameters.append("include_video", "false")
                    parameters.append("language", "en-US")
                    parameters.append("page", "1")
                    parameters.append("sort_by", "popularity.desc")
                    parameters.append("with_genres", "16")
                }
                accept(ContentType.Application.Json)
            }.body()
        } catch (e: SerializationException) {
            // JSON parse issue → return empty list or rethrow
            emptyList()
        } catch (e: ClientRequestException) {
            // 4xx → treat as empty or throw
            emptyList()
        } catch (e: Exception) {
            // Network, etc. → rethrow or return empty
            throw e
        }
    }

    override suspend fun fetchMovieImdbById(id: String): MovieDetailDto? {
        return try {
            client.get("/movie/$id") {
                accept(ContentType.Application.Json)
                url {
                    parameters.append("language", "en-US")
                }
            }.body()
        } catch (e: ClientRequestException) {
            // 4xx errors (e.g. 404 → movie not found)
            null
        } catch (e: SerializationException) {
            // JSON parsing issues
            null
        } catch (e: Exception) {
            // Other errors—rethrow or handle as you prefer
            throw e
        }
    }

}
