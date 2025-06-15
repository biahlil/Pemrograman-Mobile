package com.example.movielist.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Movie(
    val id: Int,
    @DrawableRes val image: Int,
    @StringRes val titleText: Int,
    @StringRes val yearText: Int,
    @StringRes val descriptionText: Int,
    @StringRes val imdbLink: Int,
)