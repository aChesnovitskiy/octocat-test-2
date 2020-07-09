package com.achesnovitskiy.octocattest2.ui.repoinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.viewmodels.repoinfo.RepoInfoViewModel
import kotlinx.android.synthetic.main.fragment_repo_info.*

class RepoInfoFragment : Fragment(R.layout.fragment_repo_info) {
    private val repoInfoViewModel: RepoInfoViewModel by viewModels()
    private val repoName by lazy {
        arguments?.get("repo_name") as String
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar()
        setupViewModel()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(repo_info_toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
            }

            repo_info_toolbar.setNavigationOnClickListener {
                this.onBackPressed()
            }
        }
    }

    private fun setupViewModel() {
        repoInfoViewModel
            .apply {
                setRepoName(repoName)

                getRepoName().observe(
                    viewLifecycleOwner,
                    Observer { repoName ->
                        repo_name_text_view.text = repoName
                    }
                )
            }
    }
}