package com.achesnovitskiy.octocattest2.data.database.di

import android.content.Context
import androidx.room.Room
import com.achesnovitskiy.octocattest2.data.database.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database =
        Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        )
            .build()

    @Provides
    @Singleton
    fun provideRepoDao(database: Database) = database.reposDao

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}