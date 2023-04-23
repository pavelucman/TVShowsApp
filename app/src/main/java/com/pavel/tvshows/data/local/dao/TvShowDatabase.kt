package com.pavel.tvshows.data.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavel.tvshows.data.local.models.FavoritesTable
import com.pavel.tvshows.data.local.models.TvShowsRemoteKeysTable
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable

@Database(entities = [TrendingTvShowsTable::class, TvShowsRemoteKeysTable::class, FavoritesTable::class], version = 1)
abstract class TvShowDatabase :RoomDatabase(){

    abstract fun trendingTvShowsDao(): TrendingTvShowsDao
    abstract fun tvShowsRemoteKeysDao(): TvShowsRemoteKeysDao
    abstract fun favoriteShowsDao():FavoritesDao
}