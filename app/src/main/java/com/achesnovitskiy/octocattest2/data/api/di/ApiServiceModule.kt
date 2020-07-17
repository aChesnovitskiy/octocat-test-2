package com.achesnovitskiy.octocattest2.data.api.di

import com.achesnovitskiy.octocattest2.data.api.ApiService
import com.achesnovitskiy.octocattest2.data.api.RetrofitApiServiceImpl
import com.achesnovitskiy.octocattest2.data.api.TLSSocketFactory
import dagger.Module
import dagger.Provides

@Module
class ApiServiceModule {

    @Provides
    fun provideApiService(tlsSocketFactory: TLSSocketFactory): ApiService =
        RetrofitApiServiceImpl(tlsSocketFactory)
}