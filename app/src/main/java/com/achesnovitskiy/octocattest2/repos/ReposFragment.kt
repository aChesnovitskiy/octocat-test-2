package com.achesnovitskiy.octocattest2.repos

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.achesnovitskiy.octocattest2.App
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.data.Repo
import com.achesnovitskiy.octocattest2.extensions.hideKeyboard
import com.achesnovitskiy.octocattest2.extensions.showKeyboard
import com.achesnovitskiy.octocattest2.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_repos.*
import javax.inject.Inject

class ReposFragment : Fragment(R.layout.fragment_repos) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val reposViewModel: ReposViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ReposViewModel::class.java)
    }

    private val rootActivity: MainActivity by lazy(LazyThreadSafetyMode.NONE) {
        activity as MainActivity
    }

    private val reposAdapter: ReposAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReposAdapter { repo ->
            navigateToInfo(
                repo
            )
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (rootActivity.application as App).appComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupProgressBar()
        setupSearchView()
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupProgressBar() {
        // Set progress bar color for pre-lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawableProgress =
                DrawableCompat.wrap(repos_progress_bar.indeterminateDrawable)

            DrawableCompat.setTint(
                drawableProgress,
                ContextCompat.getColor(rootActivity, R.color.color_accent)
            )

            repos_progress_bar.indeterminateDrawable = DrawableCompat.unwrap(drawableProgress)
        }
    }

    private fun setupSearchView() {
        repos_search_button.setOnClickListener {
            reposViewModel.onSearchModeChange(true)
        }

        repos_search_back_button.setOnClickListener {
            reposViewModel.onSearchModeChange(false)
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
            reposViewModel.onReposFromApiRequest()

            repos_swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun setupViewModel() {
        reposViewModel.apply {
            stateBehaviorSubject
                .subscribe { state ->
                    bindState(state)
                }
                .let { compositeDisposable.add(it) }

            reposWithSearchObservable
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

        if (state.isSearch) {
            repos_search_layout.visibility = View.VISIBLE
            repos_search_button.visibility = View.GONE
            repos_title.visibility = View.GONE

            repos_search_edit_text.requestFocus()

            rootActivity.showKeyboard()
        } else {
            repos_search_layout.visibility = View.GONE
            repos_search_button.visibility = View.VISIBLE
            repos_title.visibility = View.VISIBLE

            repos_search_edit_text.text = null

            rootActivity.hideKeyboard()
        }
    }

    private fun navigateToInfo(repo: Repo) {
        this.findNavController()
            .navigate(
                ReposFragmentDirections.actionRepositoriesFragmentToRepoInfoFragment(
                    repo.name
                )
            )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()

        super.onDestroy()
    }
}