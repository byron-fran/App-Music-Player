package com.byrondev.musicplayer.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.byrondev.musicplayer.data.models.Song
import com.byrondev.musicplayer.viewModels.MusicViewModels
import com.byrondev.musicplayer.views.AlbumsScreen
import com.byrondev.musicplayer.views.FavoritesScreen
import com.byrondev.musicplayer.views.HomeScreen
import com.byrondev.musicplayer.views.LibraryScreen
import com.byrondev.musicplayer.views.SearchScreen
import com.byrondev.musicplayer.views.albums.AlbumDetail
import com.byrondev.musicplayer.views.songs.SongDetailPlaying


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavManager(musicViewModels: MusicViewModels, player: ExoPlayer) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LibraryScreen") {
        composable("HomeScreen") {
            HomeScreen(navController, musicViewModels)
        }
        composable("AlbumsScreen") {
            AlbumsScreen(navController, musicViewModels)
        }
        composable("LibraryScreen") {
            LibraryScreen(navController, musicViewModels)
        }
        composable("AlbumDetail/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            AlbumDetail(navController, musicViewModels, player,id)
        }
        composable("FavoritesScreen") {
            FavoritesScreen(navController, musicViewModels,)
        }
        composable("SearchScreen") {
            SearchScreen()
        }
        composable("SongDetailPlaying/{songId}",
            arguments = listOf(
                navArgument("songId") { type = NavType.IntType }
            ),

            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {

                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("songId")
            SongDetailPlaying(navController, musicViewModels, player, songId = id!!)

        }
    }
}
