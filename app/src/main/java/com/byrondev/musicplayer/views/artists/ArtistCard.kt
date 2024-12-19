package com.byrondev.musicplayer.views.artists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.byrondev.musicplayer.R
import com.byrondev.musicplayer.data.models.Artist
import com.byrondev.musicplayer.ui.theme.Gray10


@Composable
fun ArtistCard(artist : Artist, navController: NavController) {

    Card (
        shape = CircleShape,
        colors =  CardDefaults.cardColors(
            containerColor = Gray10,

        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("ArtistDetail/${artist.id}") }
    ){
        Image(
            painter = painterResource(id= R.drawable.baseline_person_24),
            contentDescription =  "",
            modifier = Modifier.fillMaxWidth(),
            alignment = Alignment.Center,

        )
        Text("${artist.name}",
            color= Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp).padding(bottom = 10.dp).align(Alignment.CenterHorizontally),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 17.sp
            )
    }

}