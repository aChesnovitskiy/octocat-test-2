package com.achesnovitskiy.octocattest2.data.api

import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Single

interface ApiService {

    fun getReposByUser(userName: String): Single<List<Repo>>
}