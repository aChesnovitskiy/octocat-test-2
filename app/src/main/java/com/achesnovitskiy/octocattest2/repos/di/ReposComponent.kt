package com.achesnovitskiy.octocattest2.repos.di

import com.achesnovitskiy.octocattest2.api.di.ApiServiceModule
import com.achesnovitskiy.octocattest2.domain.di.RepositoryModule
import com.achesnovitskiy.octocattest2.repos.ReposFragment
import dagger.Subcomponent

@ReposScope
@Subcomponent(modules = [ReposModule::class, RepositoryModule::class, ApiServiceModule::class])
interface ReposComponent {

    @Subcomponent.Builder
    interface Builder {

        fun reposModule(reposModule: ReposModule): Builder

        fun build(): ReposComponent
    }

    fun inject(fragment: ReposFragment)
}