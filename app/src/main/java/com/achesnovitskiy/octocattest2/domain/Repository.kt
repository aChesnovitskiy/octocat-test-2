package com.achesnovitskiy.octocattest2.domain

import com.achesnovitskiy.octocattest2.api.ApiService
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.repos.di.ReposScope
import io.reactivex.Single
import javax.inject.Inject

interface Repository {
    fun getReposByUser(userName: String): Single<List<Repo>>
}

@ReposScope
class RepositoryImpl @Inject constructor(private val apiService: ApiService): Repository {

    override fun getReposByUser(userName: String): Single<List<Repo>> =
        apiService.getReposByUser(userName)
}