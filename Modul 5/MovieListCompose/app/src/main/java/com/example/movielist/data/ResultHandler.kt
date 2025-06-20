package com.example.movielist.data

import com.example.movielist.domain.model.MovieError

data class ResultOrError<out T>(
    val data: T? = null,
    val error: MovieError? = null
) {
    val isSuccess: Boolean
        get() = (data != null && error == null)
}
