package com.achesnovitskiy.octocattest2.api.di

import com.achesnovitskiy.octocattest2.api.ApiService
import com.achesnovitskiy.octocattest2.api.RetrofitApiService
import com.achesnovitskiy.octocattest2.api.TLSSocketFactory
import dagger.Module
import dagger.Provides

@Module
class ApiServiceModule {

    @Provides
    fun provideApiService(tlsSocketFactory: TLSSocketFactory): ApiService =
        RetrofitApiService(tlsSocketFactory)
}