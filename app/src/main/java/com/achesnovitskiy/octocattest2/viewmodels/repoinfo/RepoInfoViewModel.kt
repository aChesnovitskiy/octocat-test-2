package com.achesnovitskiy.octocattest2.viewmodels.repoinfo

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject

class RepoInfoViewModel : ViewModel() {

    var repoName: BehaviorSubject<String> = BehaviorSubject.create()

    fun onGetRepoNameFromArgs(repoNameFromArgs: String) {
        repoName.onNext(repoNameFromArgs)
    }
}