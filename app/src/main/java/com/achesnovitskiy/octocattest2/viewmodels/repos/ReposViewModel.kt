package com.achesnovitskiy.octocattest2.viewmodels.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.repositories.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ReposViewModel : ViewModel() {

    val repos: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    val state: BehaviorSubject<ReposState> = BehaviorSubject.createDefault(
        ReposState(
            isSearch =  false,
            searchQuery =  "",
            isLoading =  false
        )
    )

    private val compositeDisposable = CompositeDisposable()

    fun onRequestReposFromApi(userName: String) {
        updateState { it.copy(isLoading = true) }

        Repository.loadReposFromApi(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { reposFromApi ->
                repos.onNext(reposFromApi)
                updateState { it.copy(isLoading = false) }
            }
            .let { compositeDisposable.add(it) }
    }


    fun getRepos(userName: String): LiveData<List<Repo>> {

        val result = MediatorLiveData<List<Repo>>()

//        val filterF = {
//            val query = state.value?.searchQuery ?: ""
//            val repos = repos1.value!!
//
//            result.value = if (query.isEmpty()) repos
//            else repos.filter { it.name.contains(query, true) }
//        }
//
//        result.addSource(repos1) { filterF.invoke() }
//        result.addSource(state) { filterF.invoke() }

        return result
    }

    fun handleSearchQuery(query: String?) {
        query ?: return
        updateState { it.copy(searchQuery = query) }
    }

    fun handleSearchMode(isSearch: Boolean) {
        updateState { it.copy(isSearch = isSearch) }
    }

    private fun updateState(update: (currentState: ReposState) -> ReposState) {
        val updatedState = update(state.value!!)

        state.onNext(updatedState)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}

data class ReposState(
    val isSearch: Boolean,
    val searchQuery: String,
    val isLoading: Boolean
)