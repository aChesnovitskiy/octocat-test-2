package com.achesnovitskiy.octocattest2.ui.repos

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.app.App.Companion.appComponent
import com.achesnovitskiy.octocattest2.data.pojo.Repo
import com.achesnovitskiy.octocattest2.extensions.hideKeyboard
import com.achesnovitskiy.octocattest2.extensions.showKeyboard
import com.achesnovitskiy.octocattest2.ui.repos.di.DaggerReposComponent
import com.achesnovitskiy.octocattest2.ui.repos.di.ReposModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_repos.*
import javax.inject.Inject

class ReposFragment : Fragment(R.layout.fragment_repos) {

    @Inject
    lateinit var reposViewModel: ReposViewModel

    private val reposAdapter: ReposAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReposAdapter { repo -> navigateToInfo(repo) }
    }

    private var disposable: Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerReposComponent
            .builder()
            .appComponent(appComponent)
            .reposModule(
                ReposModule(viewModelStoreOwner = this)
            )
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setProgressBarColorForPreLollipop()
        setupSearchView()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        disposable = CompositeDisposable(
            reposViewModel.reposStateObservable
                .subscribe(::bindState),
            reposViewModel.reposWithSearchObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repos ->
                    repos_list_is_empty_text_view.isVisible = repos.isNullOrEmpty()

                    reposAdapter.submitList(repos.sortedBy { it.name })
                }
        )
    }

    override fun onPause() {
        disposable?.dispose()

        super.onPause()
    }

    override fun onDestroy() {
        disposable?.dispose()

        super.onDestroy()
    }

    /**
     * Set progress bar color for pre-lollipop devices
     */
    private fun setProgressBarColorForPreLollipop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawableProgress =
                DrawableCompat.wrap(repos_progress_bar.indeterminateDrawable)

            DrawableCompat.setTint(
                drawableProgress,
                ContextCompat.getColor(requireActivity(), R.color.color_accent)
            )

            repos_progress_bar.indeterminateDrawable = DrawableCompat.unwrap(drawableProgress)
        }
    }

    private fun setupSearchView() {
        repos_search_button.setOnClickListener {
            reposViewModel.onSearchToggle(true)
        }

        repos_search_back_button.setOnClickListener {
            reposViewModel.onSearchToggle(false)
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
            reposViewModel.onRefresh()

            repos_swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun bindState(state: ReposState) {
        repos_progress_bar.isVisible = state.isLoading

        if (state.isSearch) {
            repos_search_layout.isVisible = true
            repos_search_button.isVisible = false
            repos_title.isVisible = false

            repos_search_edit_text.requestFocus()

            requireActivity().showKeyboard()
        } else {
            repos_search_layout.isVisible = false
            repos_search_button.isVisible = true
            repos_title.isVisible = true

            repos_search_edit_text.text = null

            requireActivity().hideKeyboard()
        }
    }

    private fun navigateToInfo(repo: Repo) {
        this.findNavController()
            .navigate(
                ReposFragmentDirections.actionRepositoriesFragmentToRepoInfoFragment(repo.name)
            )
    }
}