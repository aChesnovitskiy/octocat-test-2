package com.achesnovitskiy.octocattest2.data.database.di

import android.content.Context
import androidx.room.Room
import com.achesnovitskiy.octocattest2.data.database.ReposDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReposDatabaseModule {

    @Provides
    @Singleton
    fun provideReposDatabase(context: Context): ReposDatabase =
        Room.databaseBuilder(
            context,
            ReposDatabase::class.java,
            DATABASE_NAME
        )
            .build()

    @Provides
    @Singleton
    fun provideRepoDao(reposDatabase: ReposDatabase) = reposDatabase.reposDao

    companion object {
        const val DATABASE_NAME = "repos.db"
    }
}