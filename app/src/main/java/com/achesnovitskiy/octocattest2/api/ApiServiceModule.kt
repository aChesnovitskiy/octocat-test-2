package com.achesnovitskiy.octocattest2.api

import dagger.Module
import dagger.Provides

@Module
class ApiServiceModule {

    @Provides
    fun provideApiService(tlsSocketFactory: TLSSocketFactory): ApiService =
        RetrofitApiService(tlsSocketFactory)
}