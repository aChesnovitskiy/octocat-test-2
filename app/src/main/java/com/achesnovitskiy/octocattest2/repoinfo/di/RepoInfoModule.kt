package com.achesnovitskiy.octocattest2.repoinfo.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.achesnovitskiy.octocattest2.repoinfo.RepoInfoViewModel
import com.achesnovitskiy.octocattest2.repoinfo.RepoInfoViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class RepoInfoModule(
    private val viewModelStoreOwner: ViewModelStoreOwner,
    repoNameFromArgs: String
) {

    @Provides
    fun provideRepoInfoViewModel(repoNameFromArgs: String): RepoInfoViewModel =
        ViewModelProvider(viewModelStoreOwner)
            .get(RepoInfoViewModelImpl(repoNameFromArgs)::class.java)
}