package com.example.movielist

import android.app.Application
import timber.log.Timber
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyMovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inisialisasi Timber.
        // Ini akan menanam DebugTree, yang secara otomatis akan melakukan logging
        // dengan tag yang sesuai dan hanya aktif pada build 'debug'.
        // Pada build 'release', tidak ada log yang akan dicetak.
        Timber.plant(Timber.DebugTree())
        Timber.i("Timber is initialized.")
    }

}