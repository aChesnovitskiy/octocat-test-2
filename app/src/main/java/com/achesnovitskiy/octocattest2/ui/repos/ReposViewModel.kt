package com.achesnovitskiy.octocattest2.ui.repos

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.R
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

    val loadingStateObservable: Observable<LoadingState>

    val refreshObserver: Observer<Unit>

    val searchQueryObserver: Observer<String>

    val searchToggleObserver: Observer<Boolean>
}

class ReposViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), ReposViewModel {

    private val reposBehaviorSubject: BehaviorSubject<List<Repo>> = BehaviorSubject.create()

    private val searchQueryBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val reposIsSearchBehaviorSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)

    private val loadingStateBehaviorSubject: BehaviorSubject<LoadingState> =
        BehaviorSubject.create()

    override val reposWithSearchObservable: Observable<List<Repo>>
        get() = Observable
            .combineLatest(
                reposBehaviorSubject,
                searchQueryBehaviorSubject,
                BiFunction { repos: List<Repo>, searchQuery: String ->
                    repos.filter { repo ->
                        repo.name.contains(searchQuery, true)
                    }
                }
            )

    override val reposIsSearchObservable: Observable<Boolean>
        get() = reposIsSearchBehaviorSubject

    override val loadingStateObservable: Observable<LoadingState>
        get() = loadingStateBehaviorSubject

    override val refreshObserver: PublishSubject<Unit> = PublishSubject.create()

    override val searchQueryObserver: PublishSubject<String> = PublishSubject.create()

    override val searchToggleObserver: PublishSubject<Boolean> = PublishSubject.create()

    init {
        repository.reposObservable
            .subscribe(reposBehaviorSubject)

        searchQueryObserver
            .subscribe(searchQueryBehaviorSubject)

        searchToggleObserver
            .subscribe(reposIsSearchBehaviorSubject)

        refreshObserver
            .switchMap {
                repository.refreshCompletable
                    .andThen(
                        Observable.just(
                            LoadingState(
                                isLoading = false,
                                errorRes = null
                            )
                        )
                    )
                    .startWith(
                        LoadingState(
                            isLoading = true,
                            errorRes = null
                        )
                    )
                    .onErrorReturnItem(
                        LoadingState(
                            isLoading = false,
                            errorRes = R.string.repos_update_error_message
                        )
                    )
            }
            .subscribe(loadingStateBehaviorSubject)

        refreshObserver.onNext(Unit)
    }
}

data class LoadingState(
    val isLoading: Boolean,
    @StringRes val errorRes: Int?
)