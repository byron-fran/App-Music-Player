package com.byrondev.musicplayer.views.albums

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.byrondev.musicplayer.R
import com.byrondev.musicplayer.components.BottomBar
import com.byrondev.musicplayer.components.albums.AlbumDetailTopBar
import com.byrondev.musicplayer.components.albums.ButtonsPlayAlbum
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
    val showToolbar = remember {
        derivedStateOf {
        scrollState.firstVisibleItemIndex > 0 || (scrollState.firstVisibleItemScrollOffset > 1000)
    } }

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
            LazyColumn( state = scrollState,  modifier = Modifier.padding(bottom = 100.dp)) {
                item {
                    AlbumDetailTopBar(album, navController)
                    ButtonsPlayAlbum()


                }
                items(songs) { song ->
                    SongCard(song, onSelected = {})
                }
            }
            Spacer( modifier = Modifier.height(100.dp).fillMaxWidth())
            BottomBar(navController, modifier = Modifier.align(Alignment.BottomCenter))


            AnimatedVisibility (visible = showToolbar.value, enter =  fadeIn() + expandIn  (), exit= shrinkOut () + fadeOut())
            {
                CenterAlignedTopAppBar(
                    title = { Text(
                        "${album.title}",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis

                        ) },
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    navigationIcon = {

                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
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

