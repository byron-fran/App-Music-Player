package com.byrondev.musicplayer.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.byrondev.musicplayer.Home2
import com.byrondev.musicplayer.viewModels.MusicViewModels
import com.byrondev.musicplayer.views.AlbumsScreen
import com.byrondev.musicplayer.views.FavoritesScreen
import com.byrondev.musicplayer.views.HomeScreen
import com.byrondev.musicplayer.views.LibraryScreen
import com.byrondev.musicplayer.views.SearchScreen
import com.byrondev.musicplayer.views.albums.AlbumDetail


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavManager( musicViewModels: MusicViewModels){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LibraryScreen"){
        composable("HomeScreen"){
            HomeScreen(navController,musicViewModels)
        }
        composable("AlbumsScreen" ){
            AlbumsScreen(navController, musicViewModels)
        }
        composable ("Home2") {
            Home2( navController, musicViewModels)
        }
        composable("LibraryScreen") {
            LibraryScreen(navController, musicViewModels)
        }
        composable("AlbumDetail/{id}", arguments = listOf(
            navArgument("id"){type= NavType.IntType}
        )) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            AlbumDetail(navController, musicViewModels, id)
        }
        composable("FavoritesScreen") {
            FavoritesScreen(navController, musicViewModels)
        }
        composable("SearchScreen") {
            SearchScreen()
        }

    }
}
