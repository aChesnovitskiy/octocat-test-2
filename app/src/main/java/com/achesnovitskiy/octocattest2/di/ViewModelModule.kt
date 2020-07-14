package com.achesnovitskiy.octocattest2.di

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.viewmodels.repoinfo.RepoInfoViewModel
import com.achesnovitskiy.octocattest2.viewmodels.repos.ReposViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    abstract fun reposViewModel(reposViewModel: ReposViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RepoInfoViewModel::class)
    abstract fun repoInfoViewModel(repoInfoViewModel: RepoInfoViewModel?): ViewModel?
}
