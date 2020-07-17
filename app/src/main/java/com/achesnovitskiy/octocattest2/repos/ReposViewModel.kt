package com.achesnovitskiy.octocattest2.repos

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.di.FragmentScope
import com.achesnovitskiy.octocattest2.repositories.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface ReposViewModel {

    val reposWithSearchObservable: Observable<List<Repo>>

    val reposStateBehaviorSubject: BehaviorSubject<ReposState>

    fun onReposFromApiRequest(userName: String)

    fun onSearchQuery(query: String?)

    fun onSearchModeChange(isSearchMode: Boolean)
}

class ReposViewModelImpl @Inject constructor(userName: String) : ViewModel(), ReposViewModel {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private var disposable: Disposable? = null

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
    override val reposStateBehaviorSubject: BehaviorSubject<ReposState> =
        BehaviorSubject.createDefault(
            ReposState(
                isSearch = false,
                isLoading = false
            )
        )

    init {
        onReposFromApiRequest(userName)
    }

    override fun onReposFromApiRequest(userName: String) {
        updateState { it.copy(isLoading = true) }

        disposable = Repository.loadReposFromApi(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { reposFromApi ->
                reposBehaviorSubject.onNext(reposFromApi)

                updateState { it.copy(isLoading = false) }
            }
    }

    override fun onSearchQuery(query: String?) {
        searchQueryBehaviorSubject.onNext(query ?: "")
    }

    override fun onSearchModeChange(isSearchMode: Boolean) {
        updateState { it.copy(isSearch = isSearchMode) }
    }

    private fun updateState(update: (currentState: ReposState) -> ReposState) {
        val updatedState = update(reposStateBehaviorSubject.value!!)

        reposStateBehaviorSubject.onNext(updatedState)
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}

data class ReposState(
    val isSearch: Boolean,
    val isLoading: Boolean
)