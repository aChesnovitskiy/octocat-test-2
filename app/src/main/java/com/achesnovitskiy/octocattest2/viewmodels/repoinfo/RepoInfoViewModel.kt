package com.achesnovitskiy.octocattest2.viewmodels.repoinfo

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class RepoInfoViewModel @Inject constructor() : ViewModel() {

    val repoNameBehaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    fun onGetRepoNameFromArgs(repoNameFromArgs: String) {
        repoNameBehaviorSubject.onNext(repoNameFromArgs)
    }
}