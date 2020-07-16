package com.achesnovitskiy.octocattest2.di

import com.achesnovitskiy.octocattest2.api.di.ApiServiceModule
import com.achesnovitskiy.octocattest2.repoinfo.RepoInfoFragment
import com.achesnovitskiy.octocattest2.repos.ReposFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, ViewModelModule::class, ApiServiceModule::class])
interface AppComponent {

//    fun apiServiceComponent(): ApiServiceComponent.Factory

    fun inject(fragment: RepoInfoFragment)
    fun inject(fragment: ReposFragment)
}