package com.achesnovitskiy.octocattest2.ui.repos

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import com.achesnovitskiy.octocattest2.domain.Repository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface ReposViewModel {

    val reposWithSearchObservable: Observable<List<Repo>>

    val reposIsSearchObservable: Observable<Boolean>

    val refreshObservable: Observable<Unit>

    val refreshObserver: Observer<Unit>

    val searchQueryObserver: Observer<String>

    val searchToggleObserver: Observer<Boolean>
}

class ReposViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), ReposViewModel {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

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

    override val reposIsSearchObservable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    override val refreshObservable: PublishSubject<Unit> = PublishSubject.create()

    override val refreshObserver: PublishSubject<Unit> = PublishSubject.create()

    override val searchQueryObserver: PublishSubject<String> = PublishSubject.create()

    override val searchToggleObserver: PublishSubject<Boolean> = PublishSubject.create()

    init {
        repository.reposObservable
            .subscribe(reposBehaviorSubject)

        searchQueryObserver
            .subscribe(searchQueryBehaviorSubject)

        searchToggleObserver
            .subscribe(reposIsSearchObservable)

        refreshObserver
            .flatMap {
                repository.refreshObservable
            }
            .subscribe(refreshObservable)

        refreshObserver.onNext(Unit)
    }
}