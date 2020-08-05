package com.achesnovitskiy.octocattest2.ui.repos

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import com.achesnovitskiy.octocattest2.domain.Repository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface ReposViewModel {

    val reposWithSearchObservable: Observable<List<Repo>>

    val reposIsLoadingObservable: Observable<Boolean>

    val reposIsSearchObservable: Observable<Boolean>

    val reposUpdateErrorObservable: Observable<Boolean>

    val refreshObserver: Observer<Unit>

    val searchQueryObserver: Observer<String>

    val searchToggleObserver: Observer<Boolean>
}

class ReposViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), ReposViewModel {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private var disposable: Disposable = Disposables.disposed()

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

    override val reposIsLoadingObservable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    override val reposIsSearchObservable: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    override val reposUpdateErrorObservable: BehaviorSubject<Boolean> = BehaviorSubject.create()

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

        disposable = CompositeDisposable(
            refreshObserver
                .subscribe
                {
                    reposIsLoadingObservable.onNext(true)

                    (disposable as CompositeDisposable).add(
                        repository.refreshRepos()
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                {
                                    reposIsLoadingObservable.onNext(false)

                                    reposUpdateErrorObservable.onNext(false)
                                },
                                {
                                    reposIsLoadingObservable.onNext(false)

                                    reposUpdateErrorObservable.onNext(true)
                                }
                            )
                    )
                }
        )

        refreshObserver.onNext(Unit)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}