package com.byrondev.musicplayer.components.albums

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.byrondev.musicplayer.R
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.utils.decodeBitmapWithSubsampling
import com.skydoves.cloudy.cloudy



@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AlbumDetailTopBar(album: Album?, navController: NavController) {
    val optimizedBitmap = album?.cover?.let { decodeBitmapWithSubsampling(it, 200, 200) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(370.dp)

    ) {
        Text(
            text = "Encabezado Grande",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
        if (optimizedBitmap != null) {
            Image(
                bitmap = optimizedBitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .cloudy(500)
                    .background(color = Color.Black)
                    .height(370.dp),
                alpha = 0.6f,
            )



        }
                Row(
            modifier = Modifier.padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_circle),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { navController.popBackStack() }
            )

            Text(
                "${album?.title}",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 35.dp),
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Ellipsis

            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (optimizedBitmap != null) {

                Image(
                    bitmap = optimizedBitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp),
                )

            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Text("${album?.artist}", color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text("${album?.year?.substringBefore("-")}", color = Color.White)
                Spacer(modifier = Modifier.width(10.dp) )

                Text("${album?.genres}", color = Color.White)
            }
        }

    }


}

