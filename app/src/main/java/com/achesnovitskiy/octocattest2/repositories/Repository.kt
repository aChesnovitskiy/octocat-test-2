package com.achesnovitskiy.octocattest2.repositories

import com.achesnovitskiy.octocattest2.api.ApiFactory
import com.achesnovitskiy.octocattest2.data.Repo
import io.reactivex.Single

object Repository {

    fun loadReposFromApi(userName: String): Single<List<Repo>> =
        ApiFactory.apiService.getReposByUser(userName)
}