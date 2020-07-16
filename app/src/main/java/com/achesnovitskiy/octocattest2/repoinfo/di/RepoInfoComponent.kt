package com.achesnovitskiy.octocattest2.repoinfo.di

import com.achesnovitskiy.octocattest2.repoinfo.RepoInfoFragment
import dagger.Subcomponent

@Subcomponent(modules = [RepoInfoModule::class])
interface RepoInfoComponent {

    @Subcomponent.Builder
    interface Builder {

        fun repoInfoModule(repoInfoModule: RepoInfoModule): Builder

        fun build(): RepoInfoComponent
    }

    fun inject(fragment: RepoInfoFragment)
}