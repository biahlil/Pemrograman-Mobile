package com.example.movielist.domain.model

sealed class MovieError {
    object NotFound : MovieError()
    object Network      : MovieError()
    object Unknown      : MovieError()
}
