package com.byrondev.musicplayer.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byrondev.musicplayer.data.models.Album
import com.byrondev.musicplayer.data.models.Artist
import com.byrondev.musicplayer.data.models.Song
import com.byrondev.musicplayer.data.relations.AlbumWithSongs
import com.byrondev.musicplayer.data.repositories.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import android.provider.OpenableColumns
import kotlinx.coroutines.flow.update
import java.io.FileOutputStream
import java.io.InputStream

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class MusicViewModels @Inject constructor(
    private val repository: MusicRepository,
    @ApplicationContext private val context: Context

) : ViewModel() {

    // Estado del sistema
    private val _processingState = MutableStateFlow<ProcessingState>(ProcessingState.Idle)
    val processingState: StateFlow<ProcessingState> = _processingState

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums = _albums.asStateFlow()

    private val _songDetail = MutableStateFlow(Song())
    val songDetail = _songDetail.asStateFlow()

    private val _albumWithSongs = MutableStateFlow<AlbumWithSongs?>(null)
    val albumWithSongs = _albumWithSongs.asStateFlow();

    fun clearAlbumWithSongs() {
        _albumWithSongs.value = null // Limpia el estado
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAlbums().collect { item ->

                if (item.isEmpty()) {
                    _albums.value = emptyList()
                } else {
                    _albums.value = item
                }

            }




        }
    }

    fun addMusicData(artist: Artist, album: Album, song: Song) {
        viewModelScope.launch {
            repository.addArtistWithAlbumAndSong(artist, album, song)
        }
    }

    fun getSongById (id : Int) {
        viewModelScope.launch {
            repository.getOneSongById(id).collect{item ->
                _songDetail.value = item
            }
        }
    }

    fun getAlbumByIdWithSongs(id: Int) = viewModelScope.launch {
        repository.getAlbumWithSongById(id).collect { item ->

            _albumWithSongs.value = item
        }
    }

    // Canal para gestionar las tareas
    private val taskChannel = Channel<Uri>(Channel.UNLIMITED)


    init {
        // Iniciar el procesamiento en segundo plano
        processTasks()
    }

    // Agregar una nueva tarea a la cola
    fun addTask(uri: Uri) {
        viewModelScope.launch {
            taskChannel.send(uri)
        }
    }

    // Procesar tareas desde el canal
    @RequiresApi(Build.VERSION_CODES.S)
    private fun processTasks() {
        viewModelScope.launch {
            for (uri in taskChannel) {
                _processingState.value = ProcessingState.Processing(uri)
                try {
                    handleTask(uri) // Procesar la tarea
                } catch (e: Exception) {
                    Log.e("TaskError", "Error processing URI: $uri, ${e.message}")
                } finally {
                    _processingState.value = ProcessingState.Completed(uri)
                }
            }
            _processingState.value = ProcessingState.Idle
        }
    }

    // Lógica para manejar una tarea
    @RequiresApi(Build.VERSION_CODES.S)
    private suspend fun handleTask(uri: Uri) {
        delay(10)
        Log.d("TaskProcessing", "Processing URI: $uri")
        val retriever = MediaMetadataRetriever()

        try {

            retriever.setDataSource(context, uri)
            val path = getPathFromUri(context, uri)
            Log.e("pathFile", "$path")
            val art = retriever.embeddedPicture
            val albumArtBitmap =
                art?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            val bitDepth =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITS_PER_SAMPLE)
                    ?: ""
            val sampleRate =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE)
                    ?: ""
            val bitrate =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
                    ?: ""
            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?: ""
            val trackNumber =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
            val diskNumber =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER)
                    ?: ""
            val composer =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)
            val title =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    ?: "Unknown"
            val artist =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    ?: "Unknown"
            val album =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                    ?: "Unknown"
            val dateRelease =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)
            val numTracks =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS)
                    ?: ""
            val genre =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
            val comment =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)

            val albumArtist =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)

            val qualityAudio: Int = when (bitDepth) {
                "24" -> 3
                "16" -> 2
                else -> 1

            }
            addMusicData(
                Artist(name = artist.substringBefore(",")),
                Album(
                    title = album,
                    artist = artist.substringBefore(","),
                    year = dateRelease,
                    genres = genre,
                    cover = bitmapToByteArray(albumArtBitmap),
                    albumArtist = albumArtist,
                    quality = qualityAudio
                ),
                Song(
                    title = title,
                    artist = artist,
                    audioBitDepth = bitDepth.toInt(),
                    sampleRate = sampleRate.toInt(),
                    bitRate = bitrate.toInt(),
                    cover = bitmapToByteArray(albumArtBitmap),
                    album = album,
                    year = dateRelease,
                    duration = duration.toLong(),
                    trackNumber = getTrackNumber(trackNumber),
                    uri = uri.toString(),
                    composer = composer,
                    genre = genre

                )
            )

//            navController.navigate("HomeScreen")

        } catch (error: Throwable) {
            Log.e("Error db metadata", "${error.message} artBit2")
        } finally {
            retriever.release()
        }


    }
}




sealed class ProcessingState {
    object Idle : ProcessingState()
    data class Processing(val currentTask: Uri) : ProcessingState()
    data class Completed(val lastTask: Uri) : ProcessingState()
}

fun getTrackNumber(trackNumber: String?): Int {

    return trackNumber?.substringBefore("/")?.toIntOrNull() ?: 0
}

fun bitmapToByteArray(bitmap: Bitmap?, maxWidth: Int = 500, maxHeight: Int = 500): ByteArray {

    val resizedBitmap =
        bitmap?.let { resizeBitmap(it, maxWidth, maxHeight) } // Redimensiona si es necesario
    val outputStream = ByteArrayOutputStream()
    resizedBitmap?.compress(Bitmap.CompressFormat.PNG, 90, outputStream) // Convierte a byte array
    return outputStream.toByteArray()

}

fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {

    val width = bitmap.width
    val height = bitmap.height
    val scaleFactor = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
    val scaledWidth = (width * scaleFactor).toInt()
    val scaledHeight = (height * scaleFactor).toInt()
    return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)

}


fun getPathFromUri(context: Context, uri: Uri): String? {
    val file = File(context.cacheDir, getFileName(context, uri))
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return file.path
}

fun getFileName(context: Context, uri: Uri): String {
    var name = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
    }
    return name
}