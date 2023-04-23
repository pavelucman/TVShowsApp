package com.pavel.tvshows.di

import com.pavel.tvshows.data.local.dao.TvShowDatabase
import com.pavel.tvshows.data.remote.ShowsApi
import com.pavel.tvshows.data.remote.ShowsApi.Companion.BASE_URL
import com.pavel.tvshows.data.repository.DefaultRepository
import com.pavel.tvshows.data.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): ShowsApi {
        return retrofit.create(ShowsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultRepository(
        api: ShowsApi,
        database: TvShowDatabase
    ) = MoviesRepository(api, database) as DefaultRepository
}