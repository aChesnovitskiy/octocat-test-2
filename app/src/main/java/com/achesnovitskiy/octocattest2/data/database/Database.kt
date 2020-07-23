package com.achesnovitskiy.octocattest2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achesnovitskiy.octocattest2.data.pojo.Repo

@Database(
    entities = [Repo::class],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val reposDao: ReposDao
}