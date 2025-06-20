package com.example.movielist.data.ktor

import com.example.movielist.data.ktor.dto.MovieDetailDto
import com.example.movielist.data.ktor.dto.MovieDto

interface ApiService {
    /** GET /3/discover/movie → List of MovieDto */
    suspend fun fetchAllMovies(): List<MovieDto>

    /** GET /3/movies/{id} → single MovieDto or 404 */
    suspend fun fetchMovieImdbById(id: String): MovieDetailDto?
}
