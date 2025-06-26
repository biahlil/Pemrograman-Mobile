package com.example.movielist.domain.model

data class Movie(
    val id: String,
    val imageUrl: String,
    val title: String,
    val year: String,
    val description: String,
    val imdbURL: String? = null
)