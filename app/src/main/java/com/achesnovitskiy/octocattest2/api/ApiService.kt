package com.achesnovitskiy.octocattest2.api

import com.achesnovitskiy.octocattest2.data.Repo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("users/{username}/repos")
    fun getReposByUser(@Path("username") userName: String): Single<List<Repo>>
}