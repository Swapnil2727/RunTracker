package com.example.runtracker.di

import android.content.Context
import androidx.room.Room
import com.example.runtracker.Constants.RUNNING_DATABASE_NAME
import com.example.runtracker.R
import com.example.runtracker.database.RunningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext context: Context)
            = Room.databaseBuilder(context,
        RunningDatabase::class.java, RUNNING_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRunDao(db: RunningDatabase) = db.getRunDao()
}