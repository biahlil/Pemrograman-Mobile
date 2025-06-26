package com.example.movielist.data.ktor

import com.example.movielist.data.ktor.dto.MovieDetailDto
import com.example.movielist.data.ktor.dto.MovieDto
import com.example.movielist.data.ktor.dto.MovieListResponseDto

interface ApiService {
//    GET /3/discover/movie → List of MovieDto
    suspend fun getDiscoverMovies(): MovieListResponseDto

//    GET /3/movies/{id} → single MovieDto or 404
    suspend fun getMovieDetail(id: Int): MovieDto?
}
