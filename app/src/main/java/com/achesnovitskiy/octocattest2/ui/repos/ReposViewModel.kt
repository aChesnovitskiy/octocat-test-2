package com.achesnovitskiy.octocattest2.ui.repos

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import com.achesnovitskiy.octocattest2.domain.Repository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface ReposViewModel {

    val reposWithSearchObservable: Observable<List<Repo>>

    val reposStateObservable: Observable<ReposState>

    val refreshObserver: Observer<Unit>

    val searchQueryObserver: Observer<String?>

    val searchToggleObserver: Observer<Boolean>
}

class ReposViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), ReposViewModel {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private var compositeDisposable: CompositeDisposable? = null

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

    override val refreshObserver: PublishSubject<Unit> = PublishSubject.create()

    override val searchQueryObserver: PublishSubject<String?> = PublishSubject.create()

    override val searchToggleObserver: PublishSubject<Boolean> = PublishSubject.create()

    init {
        compositeDisposable = CompositeDisposable(
            repository.reposObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repos ->
                    reposBehaviorSubject.onNext(repos)
                },
            refreshObserver
                .subscribe {
                    updateState { it.copy(isLoading = true) }

                    compositeDisposable?.add(
                        repository.refreshRepos()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                updateState { it.copy(isLoading = false) }
                            }
                    )
                },
            searchQueryObserver
                .subscribe { query ->
                    searchQueryBehaviorSubject.onNext(query ?: "")
                },
            searchToggleObserver
                .subscribe { isSearch ->
                    updateState { it.copy(isSearch = isSearch) }
                }
        )

        refreshObserver.onNext(Unit)
    }

    override fun onCleared() {
        compositeDisposable?.dispose()
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