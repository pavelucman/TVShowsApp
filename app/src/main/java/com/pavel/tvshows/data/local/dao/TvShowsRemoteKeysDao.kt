package com.pavel.tvshows.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavel.tvshows.data.local.models.TvShowsRemoteKeysTable

@Dao
interface TvShowsRemoteKeysDao {

    @Query("SELECT * FROM movies_remote_keys WHERE id =:id ")
    suspend fun getRemoteKeys(id: String): TvShowsRemoteKeysTable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<TvShowsRemoteKeysTable>)

    @Query("DELETE FROM movies_remote_keys")
    suspend fun deleteAllRemoteKeys()
}