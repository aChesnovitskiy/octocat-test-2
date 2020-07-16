package com.achesnovitskiy.octocattest2.repos

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.repositories.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ReposViewModel @Inject constructor() : ViewModel() {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val compositeDisposable = CompositeDisposable()

    val reposWithSearchObservable: Observable<List<Repo>> = Observable
        .combineLatest(
            reposBehaviorSubject,
            searchQueryBehaviorSubject,
            BiFunction { repos: List<Repo>, searchQuery: String ->
                repos.filter { repo ->
                    repo.name.contains(searchQuery, true)
                }
            }
        )

    val stateBehaviorSubject: BehaviorSubject<ReposState> = BehaviorSubject.createDefault(
        ReposState(
            isSearch = false,
            isLoading = false
        )
    )

    init {
        onReposFromApiRequest()
    }

    fun onReposFromApiRequest() {
        updateState { it.copy(isLoading = true) }

        Repository.loadReposFromApi(USER_OCTOCAT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { reposFromApi ->
                reposBehaviorSubject.onNext(reposFromApi)

                updateState { it.copy(isLoading = false) }
            }
            .let { compositeDisposable.add(it) }
    }

    fun onSearchQuery(query: String?) {
        searchQueryBehaviorSubject.onNext(query ?: "")
    }

    fun onSearchModeChange(isSearchMode: Boolean) {
        updateState { it.copy(isSearch = isSearchMode) }
    }

    private fun updateState(update: (currentState: ReposState) -> ReposState) {
        val updatedState = update(stateBehaviorSubject.value!!)

        stateBehaviorSubject.onNext(updatedState)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    companion object {
        const val USER_OCTOCAT = "octocat"
    }
}

data class ReposState(
    val isSearch: Boolean,
    val isLoading: Boolean
)