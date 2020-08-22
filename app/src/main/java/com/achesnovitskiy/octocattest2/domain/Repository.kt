package com.achesnovitskiy.octocattest2.domain

import com.achesnovitskiy.octocattest2.data.api.Api
import com.achesnovitskiy.octocattest2.data.db.Db
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface Repository {
    val reposObservable: Observable<List<Repo>>

    val refreshObservable: Observable<Unit>
}

private const val USER_OCTOCAT = "octocat"

class RepositoryImpl @Inject constructor(
    api: Api,
    private val db: Db
) : Repository {

    override val reposObservable: Observable<List<Repo>>
        get() = db.reposDao.getRepos()

    override val refreshObservable: Observable<Unit> = api.getReposByUser(USER_OCTOCAT)
        .doOnNext { repos ->
            db.runInTransaction {
                db.reposDao.clearRepos()
                db.reposDao.insertRepos(repos)
            }
        }
        .map { Unit }
        .subscribeOn(Schedulers.io())
}