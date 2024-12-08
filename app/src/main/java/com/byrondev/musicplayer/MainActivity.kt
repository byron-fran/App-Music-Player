package com.byrondev.musicplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.exoplayer.ExoPlayer
import com.byrondev.musicplayer.navigation.NavManager
import com.byrondev.musicplayer.ui.theme.MusicPlayerTheme
import com.byrondev.musicplayer.viewModels.MusicViewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModels: MusicViewModels by viewModels()

        // Create the ExoPlayer instance
        // Inicializar ExoPlayer
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true) // Pausa si se desconectan auriculares
            .build()

        enableEdgeToEdge()

        setContent {
            MusicPlayerTheme {
                NavManager(viewModels, player)
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        // Liberar el reproductor
        player.release()
    }
}





