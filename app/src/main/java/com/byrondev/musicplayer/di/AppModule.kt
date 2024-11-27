package com.byrondev.musicplayer.di

import android.content.Context
import androidx.room.Room
import com.byrondev.musicplayer.data.dao.AlbumsDao
import com.byrondev.musicplayer.data.dao.ArtistsDao
import com.byrondev.musicplayer.data.dao.SongDao
import com.byrondev.musicplayer.data.database.MusicDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerDatabase(@ApplicationContext context: Context): MusicDB {
        return Room.databaseBuilder(
            context,
            MusicDB::class.java,
            name = "music_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAlbumsDao(database: MusicDB): AlbumsDao = database.albumDao()

    @Singleton
    @Provides
    fun provideSongsDao(database: MusicDB): SongDao = database.songDao()

    @Singleton
    @Provides
    fun providerArtistDao(database: MusicDB) : ArtistsDao = database.artistDao()
}