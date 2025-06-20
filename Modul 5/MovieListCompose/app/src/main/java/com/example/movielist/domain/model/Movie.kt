package com.example.movielist.domain.model

data class Movie(
    val id: String,
    val imageUrl: String,
    val titleText: String,
    val yearText: String,
    val descriptionText: String,
    val imdbURL: String? = null
)