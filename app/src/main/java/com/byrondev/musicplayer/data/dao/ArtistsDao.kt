package com.byrondev.musicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.byrondev.musicplayer.data.models.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistsDao {

    @Query("SELECT * FROM artists")
    fun getAllArtist () : Flow<List<Artist>>

    @Query("SELECT * FROM artists WHERE name=:name COLLATE NOCASE")
    suspend  fun getArtist(name : String) : Artist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: Artist) : Long


}