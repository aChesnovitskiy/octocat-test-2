package com.achesnovitskiy.octocattest2.repoinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.achesnovitskiy.octocattest2.App
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.MainActivity
import com.achesnovitskiy.octocattest2.repoinfo.di.RepoInfoModule
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_repo_info.*
import javax.inject.Inject

class RepoInfoFragment : Fragment(R.layout.fragment_repo_info) {

    private val repoNameFromArgs: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.get("repo_name") as String
    }

    @Inject
    lateinit var repoInfoViewModel: RepoInfoViewModel

    private var disposable: Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as App).appComponent
            .repoInfoComponent()
            .repoInfoModule(RepoInfoModule(this, repoNameFromArgs))
            .build()
            .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        repo_info_back_button.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        disposable = repoInfoViewModel.repoNameObservable
            .subscribe(repo_name_text_view::setText)
    }

    override fun onStop() {
        disposable?.dispose()

        super.onStop()
    }
}