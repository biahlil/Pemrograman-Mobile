package com.example.movielist.data

import com.example.movielist.data.ktor.dto.MovieFromListDto
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.domain.model.Movie

fun MovieFromListDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title ?: "Unknown Title",
        overview = this.overview ?: "No overview available.",
        posterPath = this.posterPath ?: "",
        releaseDate = this.releaseDate ?: "Unknown Date"
    )
}

// Mengubah dari Entity (database) ke Domain Model (untuk UI)
fun MovieEntity.toDomainMovie(): Movie {
    val imdbUrl = if (this.imdbId?.isNotEmpty() == true) {
        "https://www.imdb.com/title/${this.imdbId}/"
    } else {
        ""
    }

    return Movie(
        id = this.id.toString(),
        title = this.title,
        description = this.overview,
        imageUrl = if (this.posterPath.isNotEmpty()) "https://image.tmdb.org/t/p/original${this.posterPath}" else "",
        year = this.releaseDate.split("-").firstOrNull() ?: "N/A",
        imdbURL = imdbUrl
    )
}