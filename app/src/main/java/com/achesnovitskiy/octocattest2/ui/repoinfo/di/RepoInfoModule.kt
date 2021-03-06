package com.achesnovitskiy.octocattest2.ui.repoinfo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.achesnovitskiy.octocattest2.ui.di.ViewScope
import com.achesnovitskiy.octocattest2.ui.repoinfo.RepoInfoViewModel
import com.achesnovitskiy.octocattest2.ui.repoinfo.RepoInfoViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class RepoInfoModule(
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val repoName: String
) {

    @Provides
    @ViewScope
    fun provideRepoInfoViewModel(): RepoInfoViewModel =
        ViewModelProvider(
            viewModelStoreOwner,
            RepoInfoViewModelFactory(repoName)
        )
            .get(RepoInfoViewModelImpl::class.java)

    class RepoInfoViewModelFactory(private val repoName: String) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            RepoInfoViewModelImpl(repoName) as T
    }
}