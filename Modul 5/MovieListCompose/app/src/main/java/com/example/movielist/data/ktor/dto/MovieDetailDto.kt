package com.example.movielist.data.ktor.dto

import com.example.movielist.domain.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailDto(
    @SerialName("imdb_id")
    val imdbId: String? = null

//    Could add other fields if needed later…
) {
//    Convert into existing domain Movie
    fun toDomain(existing: Movie): Movie = existing.copy(
        imdbURL = "https://www.imdb.com/title/$imdbId"
    )
}
