package com.byrondev.musicplayer.data.repositories

import androidx.room.Transaction
import com.byrondev.musicplayer.data.dao.AlbumsDao
import com.byrondev.musicplayer.data.dao.ArtistsDao
import com.byrondev.musicplayer.data.dao.SongDao
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.models.Artist
import com.byrondev.musicplayer.data.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val albumsDao: AlbumsDao,
    private val songsDao: SongDao,
    private val artistsDao: ArtistsDao
) {
    //Albums repositories
    fun getAllAlbums(): Flow<List<Album>> =
        albumsDao.getAllAlbums().flowOn(Dispatchers.IO).conflate()

    fun getAlbumById(id: Int) = albumsDao.getAlbumWithSongs(id)
    fun getAlbumWithSongById(id: Int) = albumsDao.getAlbumWithSongs2(id)


    fun getAllArtists(): Flow<List<Artist>> =
        artistsDao.getAllArtist().flowOn(Dispatchers.IO).conflate()

    suspend fun addArtistWithAlbumAndSong(artist: Artist, album: Album, song: Song) {
        insertArtistAlbumsAndSong(artist, album, song)
    }

    @Transaction
    suspend fun insertArtistAlbumsAndSong(artist: Artist, album: Album, song: Song) {
        var artistId: Int = 0
        var albumId: Int = 0

        try {
            // Check if artist exist
            val existingArtist = artist.name?.let { artistsDao.getArtist(it.substringBefore(",")) }
            artistId = existingArtist?.id ?: artistsDao.insertArtist(artist).toInt()

            // Check if Album exist
            val existingAlbum = album.title?.let { album.albumArtist?.let { it1 ->
                albumsDao.getAlbumByTitle(it,
                    it1.substringBefore(",")
                )
            } }
            albumId = existingAlbum?.id ?: albumsDao.insertAlbum(
                Album(
                    artistId = artistId,
                    title = album.title,
                    year = album.year,
                    artist = song.artist?.substringBefore(",") ?: "unknown",
                    quality = album.quality,
                    cover = song.cover,
                    genres = album.genres,
                    albumArtist = album.albumArtist
                )
            ).toInt()
            //Check if song already exist
            val songExist = song.title?.let { songsDao.getSongByTitle(it, song.album ?: "") }
            if (songExist == null) {
                songsDao.insertSong(
                    Song(
                        artistId = artistId,
                        albumId = albumId,
                        title = song.title,
                        artist = song.artist,
                        cover = song.cover,
                        duration = song.duration,
                        trackNumber = song.trackNumber,
                        bitRate = song.bitRate,
                        sampleRate = song.sampleRate,
                        audioBitDepth = song.audioBitDepth,
                        disk = song.disk,
                        uri = song.uri,
                        year = song.year,
                        album = song.album,

                        )
                )
            }

        } catch (error: Throwable) {
            println("----Error ${error.message}")
        }

    }
}