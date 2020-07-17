package com.achesnovitskiy.octocattest2.repoinfo.di

import androidx.lifecycle.ViewModel
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
        ViewModelProvider(viewModelStoreOwner, RepoInfoViewModelFactory(repoNameFromArgs))
            .get(RepoInfoViewModelImpl::class.java)

    class RepoInfoViewModelFactory(private val repoNameFromArgs: String) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            RepoInfoViewModelImpl(repoNameFromArgs) as T
    }
}