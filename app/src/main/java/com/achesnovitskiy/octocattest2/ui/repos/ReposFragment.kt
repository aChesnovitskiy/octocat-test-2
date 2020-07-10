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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.viewmodels.repos.ReposViewModel
import kotlinx.android.synthetic.main.fragment_repos.*
import rx.subjects.BehaviorSubject

class ReposFragment : Fragment(R.layout.fragment_repos) {

    private val reposViewModel: ReposViewModel by viewModels()

    private val reposAdapter: ReposAdapter by lazy {
        ReposAdapter { repo ->
            navigateToInfo(repo)
        }
    }

//    private val binding: ReposBinding by lazy { ReposBinding() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar()
        setupProgressBar()
        setupViewModel()
        setupRecyclerView()
    }

    private fun setupToolbar() {
//        (activity as AppCompatActivity).setSupportActionBar(repos_toolbar)
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

//        binding.isLoading.subscribe { isLoading ->
//            repos_progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
    }

    private fun setupViewModel() {
        reposViewModel.apply {
//                getState().observe(
//                    viewLifecycleOwner,
//                    Observer { state ->
//                        binding.bind(state)
//                    }
//                )

                getRepos(USER_OCTOCAT).observe(
                    viewLifecycleOwner,
                    Observer { repos ->
                        repos_list_is_empty_text_view.visibility =
                            if (repos.isNullOrEmpty()) View.VISIBLE else View.GONE

                        reposAdapter.submitList(repos)
                    }
                )
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

    companion object {
        const val USER_OCTOCAT = "octocat"
    }

//    inner class ReposBinding {
//        var searchQuery: String? = null
//        var isSearch: Boolean = false
//        var isLoading: BehaviorSubject<Boolean> = BehaviorSubject.create(false)
//
//        fun bind(state: ReposState) {
//            isSearch = state.isSearch
//            searchQuery = state.searchQuery
//            isLoading.onNext(state.isLoading)
//        }
//    }
}