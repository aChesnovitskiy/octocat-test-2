package com.achesnovitskiy.octocattest2.di

import com.achesnovitskiy.octocattest2.ui.repoinfo.RepoInfoFragment
import com.achesnovitskiy.octocattest2.ui.repos.ReposFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    fun inject(fragment: RepoInfoFragment)
    fun inject(fragment: ReposFragment)
}