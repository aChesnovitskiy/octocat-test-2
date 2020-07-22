package com.achesnovitskiy.octocattest2.domain.di

import com.achesnovitskiy.octocattest2.data.api.ApiService
import com.achesnovitskiy.octocattest2.domain.Repository
import com.achesnovitskiy.octocattest2.domain.RepositoryImpl
import com.achesnovitskiy.octocattest2.ui.repos.di.ReposScope
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @ReposScope
    fun provideRepository(apiService: ApiService): Repository = RepositoryImpl(apiService)
}