package com.achesnovitskiy.octocattest2.ui.repos

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import kotlinx.android.synthetic.main.fragment_repos.*

class ReposFragment : Fragment(R.layout.fragment_repos) {

    private val reposViewModel: ReposViewModel by viewModels()

    private val reposAdapter: ReposAdapter by lazy {
        ReposAdapter { repo -> navigateToInfo(repo) }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupProgressBar()
        setupSearchView()
        setupRecyclerView()
        setupViewModel()

        reposViewModel.onReposFromApiRequest(USER_OCTOCAT)
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
    }

    private fun setupSearchView() {
        repos_search_button.setOnClickListener {
            reposViewModel.onSearchModeRequest(true)
        }

        repos_search_back_button.setOnClickListener {
            reposViewModel.onSearchModeRequest(false)
        }

        repos_search_close_button.setOnClickListener {
            repos_search_edit_text.text = null
        }

        repos_search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                repos_search_close_button.isEnabled = !text.isNullOrEmpty()

                reposViewModel.onSearchQuery(text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }

    private fun setupRecyclerView() {
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        with(repos_recycler_view) {
            adapter = reposAdapter

            layoutManager = LinearLayoutManager(context)

            addItemDecoration(divider)
        }

        repos_swipe_refresh_layout.setOnRefreshListener {
            reposViewModel.onReposFromApiRequest(USER_OCTOCAT)

            repos_swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun setupViewModel() {
        reposViewModel.apply {
            state
                .subscribe { state ->
                    bindState(state)
                }
                .let { compositeDisposable.add(it) }

            reposWithSearch
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repos ->
                    repos_list_is_empty_text_view.visibility =
                        if (repos.isNullOrEmpty()) View.VISIBLE else View.GONE

                    reposAdapter.submitList(repos)
                }
                .let { compositeDisposable.add(it) }
        }
    }

    private fun bindState(state: ReposState) {
        repos_progress_bar.visibility =
            if (state.isLoading) View.VISIBLE else View.GONE

        if (state.isSearching) {
            repos_search_layout.visibility = View.VISIBLE
            repos_search_button.visibility = View.GONE
            repos_title.visibility = View.GONE

            repos_search_edit_text.requestFocus()

            ((activity as AppCompatActivity)
                .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .toggleSoftInput(
                    InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
        } else {
            repos_search_layout.visibility = View.GONE
            repos_search_button.visibility = View.VISIBLE
            repos_title.visibility = View.VISIBLE

            repos_search_edit_text.text = null
        }
    }

    private fun navigateToInfo(repo: Repo) {
        this.findNavController()
            .navigate(
                ReposFragmentDirections
                    .actionRepositoriesFragmentToRepoInfoFragment(repo.name)
            )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()

        super.onDestroy()
    }

    companion object {
        const val USER_OCTOCAT = "octocat"
    }
}