package com.example.movielist.data.ktor.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponseDto(
    val page: Int,

    @SerialName("results")  val listMovie: List<MovieDto>,
    @SerialName("total_pages")   val totalPages: Int,
)