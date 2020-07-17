package com.achesnovitskiy.octocattest2.api

import android.os.Build
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.repos.di.ReposScope
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject

interface RetrofitApiService {

    @GET("users/{username}/repos")
    fun getRetrofitReposByUser(@Path("username") userName: String): Single<List<Repo>>
}

@ReposScope
class RetrofitApiServiceImpl @Inject constructor(private val tlsSocketFactory: TLSSocketFactory) :
    ApiService {

    override fun getReposByUser(userName: String): Single<List<Repo>> =
        Retrofit.Builder()
            .let {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    it.client(
                        OkHttpClient.Builder()
                            .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager!!)
                            .build()
                    )
                } else {
                    it
                }
            }
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitApiService::class.java)
            .getRetrofitReposByUser(userName)

    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }
}


