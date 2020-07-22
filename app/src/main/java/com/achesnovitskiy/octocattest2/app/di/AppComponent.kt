package com.achesnovitskiy.octocattest2.app.di

import com.achesnovitskiy.octocattest2.data.api.ApiService
import com.achesnovitskiy.octocattest2.data.api.di.ApiServiceModule
import com.achesnovitskiy.octocattest2.domain.Repository
import com.achesnovitskiy.octocattest2.domain.di.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ApiServiceModule::class])
interface AppComponent {

    val repository: Repository

    val apiService: ApiService
}