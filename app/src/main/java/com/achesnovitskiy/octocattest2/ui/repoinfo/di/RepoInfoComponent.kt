package com.achesnovitskiy.octocattest2.ui.repoinfo.di

import com.achesnovitskiy.octocattest2.ui.repoinfo.RepoInfoFragment
import dagger.Component

@RepoInfoScope
@Component(modules = [RepoInfoModule::class])
interface RepoInfoComponent {

    fun inject(fragment: RepoInfoFragment)
}