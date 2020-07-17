package com.achesnovitskiy.octocattest2.ui.repoinfo.di

import com.achesnovitskiy.octocattest2.ui.repoinfo.RepoInfoFragment
import dagger.Subcomponent

@RepoInfoScope
@Subcomponent(modules = [RepoInfoModule::class])
interface RepoInfoComponent {

    @Subcomponent.Builder
    interface Builder {

        fun repoInfoModule(repoInfoModule: RepoInfoModule): Builder

        fun build(): RepoInfoComponent
    }

    fun inject(fragment: RepoInfoFragment)
}