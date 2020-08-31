package com.achesnovitskiy.octocattest2.domain.di

import com.achesnovitskiy.octocattest2.data.api.Api
import com.achesnovitskiy.octocattest2.data.db.Db
import com.achesnovitskiy.octocattest2.domain.Repository
import com.achesnovitskiy.octocattest2.domain.RepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: Api, db: Db): Repository =
        RepositoryImpl(api, db)
}