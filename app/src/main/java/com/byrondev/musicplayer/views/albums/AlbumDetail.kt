package com.byrondev.musicplayer.views.albums

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.byrondev.musicplayer.R
import com.byrondev.musicplayer.components.BottomBar
import com.byrondev.musicplayer.components.albums.AlbumDetailTopBar
import com.byrondev.musicplayer.components.songs.SongCard
import com.byrondev.musicplayer.ui.theme.Gray10
import com.byrondev.musicplayer.viewModels.MusicViewModels

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)

@Composable
fun AlbumDetail(
    navController: NavController,
    musicViewModels: MusicViewModels,
    player: ExoPlayer,
    id: Int
) {
    val albumWithSongs by musicViewModels.albumWithSongs.collectAsState()
    val scrollState = rememberLazyListState()
    val showToolbar = scrollState.firstVisibleItemIndex > 0 ||
            scrollState.firstVisibleItemScrollOffset > 900

    LaunchedEffect(id) {
        musicViewModels.clearAlbumWithSongs()
        musicViewModels.getAlbumByIdWithSongs(id)
    }

    if (albumWithSongs != null) {
        val songs = albumWithSongs!!.songs
        val album = albumWithSongs!!.album
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)) {
            LazyColumn(state = scrollState) {
                item {
                    AlbumDetailTopBar(album, navController)

                }
                items(songs) { song ->
                    SongCard(song, onSelected = {})
                }
            }
            BottomBar(navController, modifier = Modifier.align(Alignment.BottomCenter))

            AnimatedVisibility(showToolbar) {
                CenterAlignedTopAppBar(
                    title = { Text("${album.title}", textAlign = TextAlign.Center) },
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    navigationIcon = {

                        Icon(
                            painter = painterResource(id = R.drawable.outline_arrow_circle),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(35.dp)
                                .clickable { navController.popBackStack() }
                        )

                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Gray10)
                )
            }
        }
    } else {
        Text("Loading...")
    }

}

