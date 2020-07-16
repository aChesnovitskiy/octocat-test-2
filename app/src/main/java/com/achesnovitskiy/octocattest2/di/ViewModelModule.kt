package com.achesnovitskiy.octocattest2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.achesnovitskiy.octocattest2.viewmodels.ViewModelFactory
import com.achesnovitskiy.octocattest2.repoinfo.RepoInfoViewModel
import com.achesnovitskiy.octocattest2.repos.ReposViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    internal abstract fun reposViewModel(reposViewModel: ReposViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RepoInfoViewModel::class)
    internal abstract fun repoInfoViewModel(repoInfoViewModel: RepoInfoViewModel): ViewModel
}
