package com.achesnovitskiy.octocattest2.ui.repoinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.achesnovitskiy.octocattest2.App
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.ui.MainActivity
import com.achesnovitskiy.octocattest2.viewmodels.repoinfo.RepoInfoViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_repo_info.*
import javax.inject.Inject

class RepoInfoFragment : Fragment(R.layout.fragment_repo_info) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val repoInfoViewModel: RepoInfoViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RepoInfoViewModel::class.java)
    }

    private val rootActivity: MainActivity by lazy(LazyThreadSafetyMode.NONE) {
        activity as MainActivity
    }

    private val repoNameFromArgs: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.get("repo_name") as String
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (rootActivity.application as App).appComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupBackButton()
        setupViewModel()
    }

    private fun setupBackButton() {
        repo_info_back_button.setOnClickListener {
            rootActivity.onBackPressed()
        }
    }

    private fun setupViewModel() {
        repoInfoViewModel.apply {
            onGetRepoNameFromArgs(repoNameFromArgs)

            repoNameBehaviorSubject
                .subscribe(repo_name_text_view::setText)
                .let { compositeDisposable.add(it) }
        }
    }

    override fun onStop() {
        compositeDisposable.dispose()

        super.onStop()
    }
}