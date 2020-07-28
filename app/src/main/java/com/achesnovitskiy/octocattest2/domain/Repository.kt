package com.achesnovitskiy.octocattest2.domain

import com.achesnovitskiy.octocattest2.data.api.Api
import com.achesnovitskiy.octocattest2.data.db.Db
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface Repository {
    fun getReposFromApi(userName: String): Single<List<Repo>>

    fun getReposFromDatabase(): Single<List<Repo>>

    fun insertReposToDatabase(repos: List<Repo>)
}

class RepositoryImpl @Inject constructor(
    private val api: Api,
    private val db: Db
): Repository {

    override fun getReposFromApi(userName: String): Single<List<Repo>> =
        api.getReposByUser(userName)

    override fun getReposFromDatabase(): Single<List<Repo>> =
        db.reposDao.getRepos()

    override fun insertReposToDatabase(repos: List<Repo>) {
        Completable.fromAction { db.reposDao.insertRepos(repos) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}