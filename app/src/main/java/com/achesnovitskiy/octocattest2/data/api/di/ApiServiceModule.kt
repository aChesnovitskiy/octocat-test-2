package com.achesnovitskiy.octocattest2.data.api.di

import android.os.Build
import com.achesnovitskiy.octocattest2.data.api.ApiService
import com.achesnovitskiy.octocattest2.data.api.TLSSocketFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(tlsSocketFactory: TLSSocketFactory): OkHttpClient =
        OkHttpClient.Builder()
            .let {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    it.sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager!!)
                } else {
                    it
                }
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }
}