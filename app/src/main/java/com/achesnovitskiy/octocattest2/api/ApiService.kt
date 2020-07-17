package com.achesnovitskiy.octocattest2.api

import com.achesnovitskiy.octocattest2.data.Repo
import io.reactivex.Single

interface ApiService {

    fun getReposByUser(userName: String): Single<List<Repo>>
}