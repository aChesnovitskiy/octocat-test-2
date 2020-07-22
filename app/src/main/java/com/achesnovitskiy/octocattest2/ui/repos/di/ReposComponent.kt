package com.achesnovitskiy.octocattest2.ui.repos.di

import com.achesnovitskiy.octocattest2.app.di.AppComponent
import com.achesnovitskiy.octocattest2.ui.repos.ReposFragment
import dagger.Component

@ReposScope
@Component(dependencies = [AppComponent::class], modules = [ReposModule::class])
interface ReposComponent {

    fun inject(fragment: ReposFragment)
}