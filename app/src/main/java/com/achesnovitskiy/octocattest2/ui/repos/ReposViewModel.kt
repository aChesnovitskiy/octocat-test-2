package com.achesnovitskiy.octocattest2.ui.repos

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import com.achesnovitskiy.octocattest2.domain.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface ReposViewModel {

    val reposWithSearchObservable: Observable<List<Repo>>

    val reposStateObservable: Observable<ReposState>

    fun onRefresh()

    fun onSearchQuery(query: String?)

    fun onSearchModeChange(isSearchMode: Boolean)
}

class ReposViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), ReposViewModel {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private var compositeDisposable = CompositeDisposable()

    override val reposWithSearchObservable: Observable<List<Repo>> = Observable
        .combineLatest(
            reposBehaviorSubject,
            searchQueryBehaviorSubject,
            BiFunction { repos: List<Repo>, searchQuery: String ->
                repos.filter { repo ->
                    repo.name.contains(searchQuery, true)
                }
            }
        )

    override val reposStateObservable: BehaviorSubject<ReposState> =
        BehaviorSubject.createDefault(
            ReposState(
                isSearch = false,
                isLoading = false
            )
        )

    init {
        compositeDisposable.add(
            repository.reposFromDb
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repos ->
                    reposBehaviorSubject.onNext(repos)
                }
        )

        onRefresh()
    }

    override fun onRefresh() {
        updateState { it.copy(isLoading = true) }

        compositeDisposable.add(
            repository.refreshDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateState { it.copy(isLoading = false) }
                }
        )
    }

    override fun onSearchQuery(query: String?) {
        searchQueryBehaviorSubject.onNext(query ?: "")
    }

    override fun onSearchModeChange(isSearchMode: Boolean) {
        updateState { it.copy(isSearch = isSearchMode) }
    }

    override fun onCleared() {
        if (compositeDisposable.size() > 0) compositeDisposable.dispose()
    }

    private fun updateState(update: (currentState: ReposState) -> ReposState) {
        val updatedState = update(reposStateObservable.value!!)

        reposStateObservable.onNext(updatedState)
    }
}

data class ReposState(
    val isSearch: Boolean,
    val isLoading: Boolean
)