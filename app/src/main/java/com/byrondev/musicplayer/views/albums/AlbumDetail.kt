package com.byrondev.musicplayer.views.albums

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.byrondev.musicplayer.components.BottomBar
import com.byrondev.musicplayer.components.albums.AlbumDetailTopBar
import com.byrondev.musicplayer.components.albums.ButtonsPlayAlbum
import com.byrondev.musicplayer.components.songs.SongCard
import com.byrondev.musicplayer.viewModels.MusicViewModels
import kotlinx.coroutines.delay

private fun Long.convertToText(): String {
    val sec = this / 1000
    val minutes = sec / 60
    val seconds = sec % 60

    val minutesString = if (minutes < 10) {
        "0$minutes"
    } else {
        minutes.toString()
    }
    val secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        seconds.toString()
    }
    return "$minutesString:$secondsString"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
    // States for audio play
    val isPlaying = remember {mutableStateOf(false)}
    val currentPosition = remember { mutableLongStateOf(0) }
    val sliderPosition = remember { mutableLongStateOf(0) }
    val totalDuration = remember { mutableLongStateOf(0) }

    // state of  slides
    val totalCountSongs = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState (pageCount = {totalCountSongs.intValue })
    val playingSongIndex = remember { mutableIntStateOf(0) }


////    effects of slides
    LaunchedEffect(pagerState.currentPage) {
        playingSongIndex.intValue = pagerState.currentPage
        player.seekTo(pagerState.currentPage, 0)
    }



    LaunchedEffect(player.currentMediaItemIndex) {
        playingSongIndex.intValue = player.currentMediaItemIndex
        pagerState.animateScrollToPage(
            playingSongIndex.intValue,
            animationSpec = tween(500)
        )
    }
//    // Effects of player
//
    LaunchedEffect(id) {
        musicViewModels.clearAlbumWithSongs()
        musicViewModels.getAlbumByIdWithSongs(id)
    }
    LaunchedEffect(player.currentPosition, player.isPlaying) {
        delay(1000)
        currentPosition.longValue = player.currentPosition
    }
    LaunchedEffect(currentPosition.longValue) {
        sliderPosition.longValue = currentPosition.longValue
    }

    LaunchedEffect(player.duration) {
        if (player.duration > 0) {
            totalDuration.longValue = player.duration
        }
    }

    LaunchedEffect(Unit) {
        if(albumWithSongs != null){
            val songs = albumWithSongs!!.songs

            songs.forEach { song ->
                val itemAudio = song.uri?.let { MediaItem.fromUri(Uri.parse(it)) }
                if (itemAudio != null) {
                    player.addMediaItem(itemAudio)


                }
            }

        }

    }
    player.prepare()

    if (albumWithSongs != null) {
        val songs = albumWithSongs!!.songs

        val album = albumWithSongs!!.album
        val songsOrderedByTrack = songs.sortedBy { it.trackNumber ?: Int.MAX_VALUE }
        totalCountSongs.intValue = albumWithSongs!!.songs.count()


        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)) {
            LazyColumn( state = scrollState,  modifier = Modifier.padding(bottom = 100.dp)) {
                item {
                    AlbumDetailTopBar(album, navController, showToolbar)
                    ButtonsPlayAlbum()

                }
                items(songsOrderedByTrack) { song ->
                    SongCard(song, onSelected = {})
                }
            }
            Spacer( modifier = Modifier.height(100.dp).fillMaxWidth())
            BottomBar(navController, modifier = Modifier.align(Alignment.BottomCenter))
        }
    } else {
        Text("Loading...")
    }

}
