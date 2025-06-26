package com.example.movielist.data.ktor.dto

import com.example.movielist.domain.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String?,

    @SerialName("overview")
    val overview: String?,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("imdb_id")
    val imdbId: String?
    )

@Serializable
data class MovieListResponseDto(
    @SerialName("results")
    val listMovie: List<MovieDto>,
)