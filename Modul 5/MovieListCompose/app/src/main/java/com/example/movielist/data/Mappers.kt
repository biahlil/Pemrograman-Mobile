package com.example.movielist.data

import com.example.movielist.data.ktor.dto.MovieDto
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.domain.model.Movie

// Mengubah dari DTO (network) ke Entity (database)
fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title ?: "Title tidak tersedia",
        overview = this.overview ?: "Deskripsi tidak tersedia",
        posterPath = this.posterPath ?: "",
        releaseDate = this.releaseDate ?: "Tahun Rilis tidak tersedia",
        imdbId = this.imdbId ?: ""
    )
}

// Mengubah dari Entity (database) ke Domain Model (untuk UI)

fun MovieEntity.toDomainMovie(): Movie {
    val imdbUrl = if (this.imdbId.isNotBlank()) {
        "https://www.imdb.com/title/${this.imdbId}/"
    } else {
        ""
    }

    return Movie(
        id = this.id.toString(),
        title = this.title,
        description = this.overview,
        imageUrl = if (this.posterPath.isNotEmpty()) "https://image.tmdb.org/t/p/w500${this.posterPath}" else "",
        year = this.releaseDate.split("-").firstOrNull() ?: "N/A",
        imdbURL = imdbUrl
    )
}