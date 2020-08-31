package com.achesnovitskiy.octocattest2.ui.repoinfo

import android.content.Context
import android.os.Bundle
import android.view.View
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.ui.base.BaseFragment
import com.achesnovitskiy.octocattest2.ui.repoinfo.di.DaggerRepoInfoComponent
import com.achesnovitskiy.octocattest2.ui.repoinfo.di.RepoInfoModule
import kotlinx.android.synthetic.main.fragment_repo_info.*
import javax.inject.Inject

class RepoInfoFragment : BaseFragment(R.layout.fragment_repo_info) {

    @Inject
    lateinit var repoInfoViewModel: RepoInfoViewModel

    private val repoName: String
        get() = arguments?.get("repo_name") as String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerRepoInfoComponent
            .builder()
            .repoInfoModule(
                RepoInfoModule(
                    viewModelStoreOwner = this,
                    repoName = repoName
                )
            )
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo_info_back_button.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        disposable = repoInfoViewModel.repoNameObservable
            .subscribe(repo_name_text_view::setText)
    }
}