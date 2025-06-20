package com.example.movielist.data.ktor.dto

import com.example.movielist.domain.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("id")       val id: Int,
    @SerialName("title")       val title: String,
    @SerialName("overview")    val overview: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("release_date")val releaseDate: String,

    ) {
    fun toDomain() = Movie(
        id = id.toString(),
        imageUrl = "https://image.tmdb.org/t/p/original$posterPath",
        titleText = title,
        yearText = releaseDate,
        descriptionText = overview
    )
}