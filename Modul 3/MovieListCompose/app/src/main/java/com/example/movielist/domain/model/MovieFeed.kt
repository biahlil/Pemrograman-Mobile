package com.example.movielist.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MovieFeed(
    val id: Int,
    @DrawableRes val image: Int,
    @StringRes val titleText: Int,
    @StringRes val yearText: Int,
    @StringRes val descriptionText: Int,
    @StringRes val imdbLink: Int,
)