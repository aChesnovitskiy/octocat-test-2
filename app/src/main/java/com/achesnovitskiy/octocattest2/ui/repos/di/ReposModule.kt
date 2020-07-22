package com.achesnovitskiy.octocattest2.ui.repos.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.achesnovitskiy.octocattest2.domain.Repository
import com.achesnovitskiy.octocattest2.ui.repos.ReposViewModel
import com.achesnovitskiy.octocattest2.ui.repos.ReposViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class ReposModule(
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val userName: String
) {

    @Provides
    @ReposScope
    fun provideReposViewModel(repository: Repository): ReposViewModel =
        ViewModelProvider(viewModelStoreOwner,
            ReposViewModelFactory(
                userName,
                repository
            )
        )
            .get(ReposViewModelImpl::class.java)

    class ReposViewModelFactory(private val userName: String, private val repository: Repository) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ReposViewModelImpl(
                userName,
                repository
            ) as T
    }
}