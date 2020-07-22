package com.achesnovitskiy.octocattest2.ui.repoinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.ui.repoinfo.di.DaggerRepoInfoComponent
import com.achesnovitskiy.octocattest2.ui.repoinfo.di.RepoInfoComponent
import com.achesnovitskiy.octocattest2.ui.repoinfo.di.RepoInfoModule
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_repo_info.*
import javax.inject.Inject

class RepoInfoFragment : Fragment(R.layout.fragment_repo_info) {

    @Inject
    lateinit var repoInfoViewModel: RepoInfoViewModel

    lateinit var repoInfoComponent: RepoInfoComponent

    private val repoName: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.get("repo_name") as String
    }

    private var disposable: Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        repoInfoComponent = DaggerRepoInfoComponent
            .builder()
            .repoInfoModule(
                RepoInfoModule(
                    viewModelStoreOwner = this,
                    repoName = repoName
                )
            )
            .build()

        repoInfoComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        repo_info_back_button.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onResume() {
        super.onResume()

        disposable = repoInfoViewModel.repoNameObservable
            .subscribe(repo_name_text_view::setText)
    }

    override fun onPause() {
        disposable?.dispose()

        super.onPause()
    }

    override fun onDestroy() {
        disposable?.dispose()

        super.onDestroy()
    }
}