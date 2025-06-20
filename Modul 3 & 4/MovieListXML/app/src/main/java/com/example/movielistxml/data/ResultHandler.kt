package com.example.movielistxml.data

sealed class ResultHandler<out R> {
    data class Success<out T>(val data: T) : ResultHandler<T>()
    data class Error(val exception: Exception) : ResultHandler<Nothing>()
}

fun <T> ResultHandler<T>.successOr(fallback: T): T {
    return (this as? ResultHandler.Success<T>)?.data ?: fallback
}