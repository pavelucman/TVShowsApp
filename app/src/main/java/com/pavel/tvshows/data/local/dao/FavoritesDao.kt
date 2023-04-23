package com.pavel.tvshows.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavel.tvshows.data.local.models.FavoritesTable
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTvShow(movie: FavoritesTable)

    @Query("DELETE FROM favorites_table WHERE favoriteId=:showId")
    fun deleteFavoriteTvShow(showId: Int)

    @Query("SELECT * FROM favorites_table")
    fun getFavoriteTvShow(): Flow<List<FavoritesTable>>

    @Query("SELECT * FROM movie_table JOIN favorites_table ON id = favoriteId AND favoriteId =:showId OR favoriteId =:showId")
    fun checkIfTvShowExists(showId: Int): Boolean
}