package com.achesnovitskiy.octocattest2.data.api

import com.achesnovitskiy.octocattest2.data.pojo.Repo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("users/{username}/repos")
    fun getReposByUser(@Path("username") userName: String): Observable<List<Repo>>
}