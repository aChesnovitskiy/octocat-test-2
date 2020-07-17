package com.achesnovitskiy.octocattest2.app.di

import com.achesnovitskiy.octocattest2.ui.repoinfo.di.RepoInfoComponent
import com.achesnovitskiy.octocattest2.ui.repos.di.ReposComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class])
interface AppComponent {

    fun reposComponent(): ReposComponent.Builder

    fun repoInfoComponent(): RepoInfoComponent.Builder
}