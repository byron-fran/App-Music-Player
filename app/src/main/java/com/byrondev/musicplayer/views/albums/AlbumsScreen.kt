package com.byrondev.musicplayer.views.albums

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.byrondev.musicplayer.components.albums.AlbumCard
import com.byrondev.musicplayer.viewModels.MusicViewModels

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AlbumsScreen(musicViewModels: MusicViewModels,) {
//    val albums by  viewModel.albums.collectAsState()
    val listAlbums = musicViewModels.albums.collectAsState().value
    val navController = rememberNavController()

    Column (modifier = Modifier.background(color = Color.Black)){
        LazyVerticalGrid  (
            columns = GridCells.Adaptive(200.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),

            ) {

//            item(span = { GridItemSpan(maxLineSpan) }) {
//
//
//            }
           items(listAlbums){album ->
               AlbumCard(album, navController)
           }
        }
    }

}
