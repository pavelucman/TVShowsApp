package com.pavel.tvshows.data.remote

import com.pavel.tvshows.data.remote.models.SearchMoviesResponse
import com.pavel.tvshows.data.remote.models.SimilarTvShowResponse
import com.pavel.tvshows.data.remote.models.TrendingMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowsApi {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org"
        const val IMAGE_SOURCE = "https://image.tmdb.org/t/p/original"
    }

    @GET("/3/trending/tv/week")
    suspend fun getTrendingTvShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): TrendingMoviesResponse

    @GET("/3/search/tv")
    suspend fun searchTvShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): SearchMoviesResponse

    @GET("/3/tv/{tvShow}/similar")
    suspend fun similarTvShows(
        @Path("tvShow") tvShow: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): SimilarTvShowResponse
}