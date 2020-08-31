package com.achesnovitskiy.octocattest2.data.db.di

import android.content.Context
import androidx.room.Room
import com.achesnovitskiy.octocattest2.data.db.Db
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): Db = Room.databaseBuilder(
        context,
        Db::class.java,
        DATABASE_NAME
    ).build()

    companion object {
        const val DATABASE_NAME = "database.db"
    }
}