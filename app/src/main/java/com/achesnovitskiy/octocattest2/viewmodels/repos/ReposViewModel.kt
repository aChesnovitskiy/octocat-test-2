package com.achesnovitskiy.octocattest2.viewmodels.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.repositories.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ReposViewModel(initialUserName: String) : ViewModel() {

    private val repos: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQuery: BehaviorSubject<String> = BehaviorSubject.create()

    private val compositeDisposable = CompositeDisposable()

    val reposWithSearch: Observable<List<Repo>> = Observable
        .combineLatest(
            repos,
            searchQuery,
            BiFunction { repos: List<Repo>, searchQuery: String ->
                repos.filter { repo ->
                    repo.name.contains(searchQuery, true)
                }
            }
        )

    val state: BehaviorSubject<ReposState> = BehaviorSubject.createDefault(
        ReposState(
            isSearching = false,
            isLoading = false
        )
    )

    init {
        onReposFromApiRequest(initialUserName)
    }

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

    fun onSearchQuery(query: String?) {
        searchQuery.onNext(query ?: "")
    }

    fun onSearchModeRequest(isSearch: Boolean) {
        updateState { it.copy(isSearching = isSearch) }
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
    val isSearching: Boolean,
    val isLoading: Boolean
)

@Suppress("UNCHECKED_CAST")
class ReposViewModelFactory(private val initialUserName: String) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ReposViewModel(initialUserName) as T
}