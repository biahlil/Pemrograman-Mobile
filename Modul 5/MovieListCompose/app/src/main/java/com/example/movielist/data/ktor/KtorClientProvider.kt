package com.example.movielist.data.ktor

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

object KtorClientProvider {
    val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            engine {
                requestTimeout = 15_000
                endpoint {
                    connectTimeout = 15_000
                    socketTimeout = 15_000
                }
            }
            // Configure default request parameters
            defaultRequest {
                 url("https://api.themoviedb.org/3/")
                headers.appendIfNameAbsent("Accept", "application/json")
                headers.appendIfNameAbsent("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwZGJkZGQ5MjBlM2FkNDU0M2RkN2ZlYjNmOWI2YzViMCIsIm5iZiI6MTc0OTAxODExNS44NzYsInN1YiI6IjY4M2ZlNjAzMDUzNjE5YTdhZGZkYmVlMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BBXzrwJuk_3W0YFiosG7wZ9uP_Ll5Zi93ZpQBd4M7iA")
            }
            // Configure client-wide settings
            expectSuccess = true
            followRedirects = true
        }
    }
}
