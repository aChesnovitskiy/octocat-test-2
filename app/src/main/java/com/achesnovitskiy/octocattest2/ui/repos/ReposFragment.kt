package com.achesnovitskiy.octocattest2.ui.repos

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.viewmodels.repos.ReposState
import com.achesnovitskiy.octocattest2.viewmodels.repos.ReposViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_repos.*

class ReposFragment : Fragment(R.layout.fragment_repos) {

    private val reposViewModel: ReposViewModel by viewModels()

    private val reposAdapter: ReposAdapter by lazy {
        ReposAdapter { repo -> navigateToInfo(repo) }
    }

    private val binding: ReposBinding by lazy { ReposBinding() }

    private val compositeDisposable = CompositeDisposable()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupProgressBar()
        setupViewModel()
        setupRecyclerView()
    }

    private fun setupProgressBar() {
        // Set progress bar color for pre-lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawableProgress =
                DrawableCompat.wrap(repos_progress_bar.indeterminateDrawable)

            DrawableCompat.setTint(
                drawableProgress,
                ContextCompat.getColor((activity as AppCompatActivity), R.color.color_accent)
            )

            repos_progress_bar.indeterminateDrawable = DrawableCompat.unwrap(drawableProgress)
        }

        binding.isLoading
            .subscribe { isLoading ->
                repos_progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            .let { compositeDisposable.add(it) }
    }

    private fun setupViewModel() {
        reposViewModel.apply {
            state
                .subscribe { state ->
                    binding.bind(state)
                }
                .let { compositeDisposable.add(it) }

            repos
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repos ->
                    repos_list_is_empty_text_view.visibility =
                        if (repos.isNullOrEmpty()) View.VISIBLE else View.GONE

                    reposAdapter.submitList(repos)
                }
                .let { compositeDisposable.add(it) }

            onRequestReposFromApi(USER_OCTOCAT)
        }
    }

    private fun setupRecyclerView() {
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        with(repos_recycler_view) {
            adapter = reposAdapter

            layoutManager = LinearLayoutManager(context)

            addItemDecoration(divider)
        }

        repos_swipe_refresh_layout.setOnRefreshListener {
            Log.d("My_ReposFragment", "Refresh")

            repos_swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun navigateToInfo(repo: Repo) {
        this.findNavController()
            .navigate(
                ReposFragmentDirections
                    .actionRepositoriesFragmentToRepoInfoFragment(repo.name)
            )
    }

    override fun onStop() {
        compositeDisposable.dispose()

        super.onStop()
    }

    companion object {
        const val USER_OCTOCAT = "octocat"
    }

    inner class ReposBinding {
        var searchQuery: String? = null
        var isSearch: Boolean = false
        var isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

        fun bind(state: ReposState) {
            isSearch = state.isSearch
            searchQuery = state.searchQuery
            isLoading.onNext(state.isLoading)
        }
    }
}