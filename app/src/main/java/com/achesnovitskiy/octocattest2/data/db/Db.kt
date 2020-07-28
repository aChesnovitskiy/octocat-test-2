package com.achesnovitskiy.octocattest2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achesnovitskiy.octocattest2.data.pojo.Repo

@Database(
    entities = [Repo::class],
    version = 1
)
abstract class Db : RoomDatabase() {

    abstract val reposDao: ReposDao
}