package com.achesnovitskiy.octocattest2.ui.repoinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.viewmodels.repoinfo.RepoInfoViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_repo_info.*

class RepoInfoFragment : Fragment(R.layout.fragment_repo_info) {

    private val repoInfoViewModel: RepoInfoViewModel by viewModels()

    private val repoNameFromArgs: String by lazy { arguments?.get("repo_name") as String }

    private lateinit var disposable: Disposable

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupBackButton()
        setupViewModel()
    }

    private fun setupBackButton() {
        repo_info_back_button.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
    }

    private fun setupViewModel() {
        repoInfoViewModel.apply {
            onGetRepoNameFromArgs(repoNameFromArgs)

            disposable = repoName.subscribe { repoName ->
                    repo_name_text_view.text = repoName
                }
        }
    }

    override fun onStop() {
        disposable.dispose()

        super.onStop()
    }
}