package com.achesnovitskiy.octocattest2.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Single

@Dao
interface ReposDao {

    @Query("SELECT * FROM repo")
    fun getRepos(): Single<List<Repo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<Repo>)

    @Query("DELETE FROM repo")
    fun clearRepos()
}