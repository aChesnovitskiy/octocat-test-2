package com.achesnovitskiy.octocattest2.di

import com.achesnovitskiy.octocattest2.repoinfo.di.RepoInfoComponent
import com.achesnovitskiy.octocattest2.repos.di.ReposComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class])
interface AppComponent {

//    fun apiServiceComponent(): ApiServiceComponent.Factory

//    fun inject(fragment: RepoInfoFragment)

    fun reposComponent(): ReposComponent.Builder

    fun repoInfoComponent(): RepoInfoComponent.Builder
}