package com.achesnovitskiy.octocattest2.viewmodels.repos

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

    fun onReposFromApiRequest(userName: String) {
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
    fun handleSearchQuery(query: String?) {
        query ?: return
        updateState { it.copy(searchQuery = query) }
    }

    fun onSearchModeRequest(isSearch: Boolean) {
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