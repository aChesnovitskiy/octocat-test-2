package com.achesnovitskiy.octocattest2.domain

import com.achesnovitskiy.octocattest2.data.api.Api
import com.achesnovitskiy.octocattest2.data.db.Db
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

interface Repository {
    val reposFromDb: Observable<List<Repo>>

    fun refreshDb(): Completable
}

class RepositoryImpl @Inject constructor(
    private val api: Api,
    private val db: Db
) : Repository {

    override val reposFromDb: Observable<List<Repo>>
        get() = db.reposDao.getRepos()

    override fun refreshDb(): Completable = api.getReposByUser(USER_OCTOCAT)
        .doAfterSuccess { repos ->
            db.runInTransaction {
                db.reposDao.clearRepos()
                db.reposDao.insertRepos(repos)
            }
        }
        .ignoreElement()

    companion object {
        const val USER_OCTOCAT = "octocat"
    }
}