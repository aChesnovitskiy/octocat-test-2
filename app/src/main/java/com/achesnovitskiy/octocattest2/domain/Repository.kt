package com.achesnovitskiy.octocattest2.domain

import com.achesnovitskiy.octocattest2.data.api.ApiService
import com.achesnovitskiy.octocattest2.data.database.ReposDao
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Single
import javax.inject.Inject

interface Repository {
    fun getReposByUser(userName: String): Single<List<Repo>>
}

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val reposDao: ReposDao
): Repository {

    override fun getReposByUser(userName: String): Single<List<Repo>> =
        apiService.getReposByUser(userName)
}