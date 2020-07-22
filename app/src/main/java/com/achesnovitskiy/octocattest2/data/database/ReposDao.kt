package com.achesnovitskiy.octocattest2.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Maybe

@Dao
interface ReposDao {

    @Query("SELECT * FROM repo")
    fun getRepos(): Maybe<List<Repo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<Repo>)
}