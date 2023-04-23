package com.pavel.tvshows.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable

@Dao
interface TrendingTvShowsDao {

    @Query("SELECT * FROM movie_table ORDER BY page ASC")
    fun getAllTvShowsPaged(): PagingSource<Int, TrendingTvShowsTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTvShow(movies: List<TrendingTvShowsTable>)

    @Query("UPDATE movie_table SET name=:name WHERE id = :movieId")
    fun updateTvShow(movieId: Int, name: String)

    @Query("DELETE FROM movie_table")
    suspend fun deleteAllTvShows()

    @Update
    suspend fun updateFavoriteShowIsFavorite(movie: TrendingTvShowsTable)

    @Query("SELECT * FROM movie_table WHERE id=:movieId")
    fun getFavoriteShowById(movieId: Int): TrendingTvShowsTable?

    @Query("SELECT EXISTS(SELECT * FROM movie_table WHERE id = :movieId)")
    fun tvShowExist(movieId: Int): Boolean
}