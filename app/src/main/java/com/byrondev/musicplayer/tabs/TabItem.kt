package com.byrondev.musicplayer.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.byrondev.musicplayer.viewModels.MusicViewModels
import com.byrondev.musicplayer.views.albums.AlbumsScreen
import com.byrondev.musicplayer.views.artists.ArtistScreen
import com.byrondev.musicplayer.views.songs.SongsScreen

typealias ComposableFun = @Composable ( MusicViewModels ) -> Unit

sealed class TabItem( var title: String, var screen: ComposableFun) {
    @RequiresApi(Build.VERSION_CODES.S)
    data object Albums : TabItem( "Albums", { musicViewModels -> AlbumsScreen(musicViewModels) } )
    data object Songs : TabItem( "Songs", {musicViewModels ->   SongsScreen(musicViewModels) })
    data object Artists : TabItem( "Artists", {musicViewModels ->  ArtistScreen(musicViewModels) })

}
