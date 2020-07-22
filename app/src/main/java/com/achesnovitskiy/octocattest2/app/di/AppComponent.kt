package com.achesnovitskiy.octocattest2.app.di

import android.content.Context
import com.achesnovitskiy.octocattest2.data.api.ApiService
import com.achesnovitskiy.octocattest2.data.api.di.ApiServiceModule
import com.achesnovitskiy.octocattest2.data.database.di.ReposDatabaseModule
import com.achesnovitskiy.octocattest2.domain.Repository
import com.achesnovitskiy.octocattest2.domain.di.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        ApiServiceModule::class,
        ReposDatabaseModule::class
    ]
)
interface AppComponent {

    val context: Context

    val repository: Repository

    val apiService: ApiService
}