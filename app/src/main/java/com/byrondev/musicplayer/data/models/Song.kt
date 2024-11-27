package com.byrondev.musicplayer.data.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "songs", indices = [Index(value = [ "album_id", "artist_id"])])
data class Song(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String? = "",

    @ColumnInfo(name = "artist")
    val artist: String? = "unknown",

    @ColumnInfo(name = "duration")
    val duration: Long = 0,

    @ColumnInfo(name = "track_number")
    val trackNumber: Int? = 0,

    @ColumnInfo(name = "year")
    val year: String? = "",

    @ColumnInfo(name = "disk")
    val disk: Int? = 0,

    @ColumnInfo(name = "cover")
    val cover: ByteArray?,

    @ColumnInfo(name = "audio_bit_depth")
    val audioBitDepth: Int? = 0,

    @ColumnInfo(name = "sample_rate")
    val sampleRate: Int = 0,

    @ColumnInfo(name = "bit_rate")
    val bitRate: Int = 0,

    @ColumnInfo(name = "audio_codec")
    val audioCodec: String? = "",

    @ColumnInfo(name = "numbers_of_channels")
    val numberOfChannels: Int = 0,

    @ColumnInfo(name = "album")
    val album: String? = "",

    @ColumnInfo("genre")
    val genre: String? = "",

    @ColumnInfo(name = "uri")
    val uri: String?,

    @ColumnInfo(name = "listeners")
    val listeners: Long = 0,

    @ColumnInfo(name = "composer")
    val composer: String? = "",

    @ColumnInfo(name = "copyright")
    val copyright: String? = "",


    @ColumnInfo(name = "album_id")
    val albumId: Int? = 0,

    @ColumnInfo(name = "artist_id")
    val artistId: Int? = 0,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        return cover.contentEquals(other.cover)
    }

    override fun hashCode(): Int {
        return cover.contentHashCode()
    }
}
