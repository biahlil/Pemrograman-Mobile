package com.example.movielist.data.ktor.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponseDto(
    @SerialName("results")
    val results: List<MovieFromListDto>
)

@Serializable
data class MovieFromListDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String?
)

@Serializable
data class MovieDetailDto(
    @SerialName("id")
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String?
)