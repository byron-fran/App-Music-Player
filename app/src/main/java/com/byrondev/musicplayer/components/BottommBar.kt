package com.byrondev.musicplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.byrondev.musicplayer.R
import com.byrondev.musicplayer.ui.theme.Gray10
import com.byrondev.musicplayer.ui.theme.Gray20
import com.byrondev.musicplayer.ui.theme.Pink40
import com.byrondev.musicplayer.ui.theme.Pink60
import com.byrondev.musicplayer.ui.theme.Pink80

data class MenuItemBottom(
    val title: String,
    val icon : Painter,
    val size : Dp = 35.dp,
    val path : String = ""

)
@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {

    val listMenuItemBottom = listOf(
        MenuItemBottom(title = "Home", icon = painterResource(id=R.drawable.home), path = "HomeScreen" ),
        MenuItemBottom(title = "Library", icon = painterResource(id=R.drawable.music), path = "LibraryScreen"),
        MenuItemBottom(title = "Favorites", icon = painterResource(id=R.drawable.favorite_filled), path="SongDetailPlaying/${6}"),
        MenuItemBottom(title = "Search", icon = painterResource(id=R.drawable.search), path = "SearchScreen"),
    )

    NavigationBar (
        modifier = modifier

            .background(color = Color.Black)
            .drawBehind {
                val strokeWidth = 2.dp.toPx() // Define el grosor del borde
                val color = Gray20 // Color del borde
                drawLine(
                    color = color,
                    start = Offset(0f, 0f), // Comienzo de la línea (izquierda superior)
                    end = Offset(size.width, 0f), // Fin de la línea (derecha superior)
                    strokeWidth = strokeWidth
                )
            },
        containerColor = Color.Black,


        // Posicionar en la parte inferior
    ) {
        listMenuItemBottom.forEach { item ->

            NavigationBarItem(
                selected = item.path == navController.currentBackStackEntry?.destination?.route.toString(),
                onClick = { navController.navigate(item.path) },
                icon = { Icon(item.icon, contentDescription = "Icon ${item.title}") },
                label = { Text(item.title) },
                modifier = Modifier.size(item.size),
                colors =  NavigationBarItemColors(
                    selectedIconColor = Pink60,
                    selectedTextColor = Pink60,
                    disabledIconColor = Color.White,
                    disabledTextColor = Color.White,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}