package com.example.movielist.di

import android.content.Context
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.fake.FakeMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(@ApplicationContext context: Context): MovieRepository {
        // Untuk menggunakan repository asli (dengan Ktor)
//        return MovieRepositoryImpl(context)

        // Untuk menggunakan repository palsu (untuk development/testing)
        // Cukup beri komentar pada baris di atas dan hapus komentar di bawah ini
         return FakeMovieRepository()
    }
}