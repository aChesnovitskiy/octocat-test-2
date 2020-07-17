package com.achesnovitskiy.octocattest2.repoinfo

import androidx.lifecycle.ViewModel
import com.achesnovitskiy.octocattest2.di.FragmentScope
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface RepoInfoViewModel {

    val repoNameObservable: Observable<String>
}

class RepoInfoViewModelImpl @Inject constructor(repoName: String) : ViewModel(),
    RepoInfoViewModel {

    override val repoNameObservable: Observable<String> =
        BehaviorSubject.createDefault(repoName)
}