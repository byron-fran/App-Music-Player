package com.byrondev.musicplayer.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.models.Artist

data class ArtistWithAlbums(
    @Embedded val artist: Artist,
    @Relation(
        parentColumn = "id",
        entityColumn = "artist_id"
    )
    val albums: List<Album>
)
