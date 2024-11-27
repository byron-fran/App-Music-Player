package com.byrondev.musicplayer.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.models.Song

data class AlbumWithSongs(
    @Embedded val album: Album, // Incluye los datos del álbum
    @Relation(
        parentColumn = "id",       // Columna en "albums" que es la clave primaria
        entityColumn = "album_id" // Columna en "songs" que hace referencia al álbum
    )
    val songs: List<Song>

)
