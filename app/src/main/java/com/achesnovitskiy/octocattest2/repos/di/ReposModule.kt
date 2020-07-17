package com.achesnovitskiy.octocattest2.repos.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.achesnovitskiy.octocattest2.di.FragmentScope
import com.achesnovitskiy.octocattest2.repos.ReposViewModel
import com.achesnovitskiy.octocattest2.repos.ReposViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class ReposModule(
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val userName: String
) {

    @Provides
    @FragmentScope
    fun provideReposViewModel(): ReposViewModel =
        ViewModelProvider(viewModelStoreOwner, ReposViewModelFactory(userName))
            .get(ReposViewModelImpl::class.java)

    class ReposViewModelFactory(private val userName: String) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ReposViewModelImpl(userName) as T
    }
}