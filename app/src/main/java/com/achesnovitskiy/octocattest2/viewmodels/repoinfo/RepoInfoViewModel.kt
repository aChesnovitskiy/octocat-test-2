package com.achesnovitskiy.octocattest2.viewmodels.repoinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RepoInfoViewModel : ViewModel() {
    private val repoName = MutableLiveData<String>()

    fun setRepoName(repoName: String) {
        this.repoName.value = repoName
    }

    fun getRepoName() : LiveData<String> = repoName
}