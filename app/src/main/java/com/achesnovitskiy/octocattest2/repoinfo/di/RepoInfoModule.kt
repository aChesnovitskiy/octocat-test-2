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
    private val repoNameFromArgs: String
) {

    @Provides
    fun provideRepoInfoViewModel(): RepoInfoViewModel =
        ViewModelProvider(viewModelStoreOwner)
            .get(RepoInfoViewModelImpl()::class.java)
}