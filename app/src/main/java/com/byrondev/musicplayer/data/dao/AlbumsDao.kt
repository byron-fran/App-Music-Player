package com.byrondev.musicplayer.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.relations.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {
    @Query("SELECT * FROM albums")
    fun getAllAlbums() : Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album) : Long

    @Query("SELECT * FROM albums WHERE title=:title AND artist = :albumArtist LIMIT 1")
    suspend fun getAlbumByTitle(title : String, albumArtist : String) : Album?

    @Query("SELECT * FROM albums WHERE id=:id")
    fun  getAlbumWithSongs(id : Int) : Flow<Album>

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :id")
    fun getAlbumWithSongs2(id: Int): Flow<AlbumWithSongs>
}