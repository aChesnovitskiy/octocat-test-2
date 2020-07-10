package com.achesnovitskiy.octocattest2.api

import android.os.Build
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://api.github.com/"

    val apiService: ApiService = Retrofit.Builder()
        .let {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val tlsSocketFactory = TLSSocketFactory()

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
        .create(ApiService::class.java)
}