package com.example.movielist.di

import android.content.Context
import androidx.room.Room
import com.example.movielist.data.ktor.ApiService
import com.example.movielist.data.ktor.ApiServiceImpl
import com.example.movielist.data.ktor.KtorClientProvider
import com.example.movielist.data.local.MovieDao
import com.example.movielist.data.local.MovieDatabase
import com.example.movielist.data.repository.MovieRepository
import com.example.movielist.data.repository.fake.FakeMovieRepository
import com.example.movielist.data.repository.network.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        return KtorClientProvider.httpClient
    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService {
        return ApiServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(@ApplicationContext context: Context, api: ApiService, dao: MovieDao): MovieRepository {
        // Untuk menggunakan repository asli (dengan Ktor)
        return MovieRepositoryImpl(api, dao)

        // Beri komentar pada baris di atas dan hapus komentar di bawah ini
//         return FakeMovieRepository(context = context)
    }
}