package com.achesnovitskiy.octocattest2.app.di

import android.content.Context
import com.achesnovitskiy.octocattest2.data.api.Api
import com.achesnovitskiy.octocattest2.data.api.di.ApiModule
import com.achesnovitskiy.octocattest2.data.database.di.DatabaseModule
import com.achesnovitskiy.octocattest2.domain.Repository
import com.achesnovitskiy.octocattest2.domain.di.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        ApiModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {

    val context: Context

    val repository: Repository

    val api: Api
}