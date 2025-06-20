package com.example.movielistxml.domain
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MovieModel(
    val id: Int,
    @DrawableRes val image: Int,
    @StringRes val titleText: Int,
    @StringRes val yearText: Int,
    @StringRes val descriptionText: Int,
    @StringRes val imdbLink: Int,
)