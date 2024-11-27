package com.byrondev.musicplayer.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.models.Artist
import com.byrondev.musicplayer.data.models.Song

data class ArtistWithSongs(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "id",
        entityColumn = "song_id"
    )
    val songs: List<Song>
)
