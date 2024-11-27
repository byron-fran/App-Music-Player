package com.byrondev.musicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.models.Artist
import com.byrondev.musicplayer.data.models.Song
import kotlinx.coroutines.flow.Flow


@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    fun getAllSongs() : Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE album_id=:id")
    fun  getAlbumWithSong(id : Int) : Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE title=:title AND album=:album")
    suspend fun getSongByTitle(title : String, album : String) : Song?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song) : Long


}