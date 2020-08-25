package com.achesnovitskiy.octocattest2.ui.repos

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.achesnovitskiy.octocattest2.R
import com.achesnovitskiy.octocattest2.app.App.Companion.appComponent
import com.achesnovitskiy.octocattest2.extensions.hideKeyboard
import com.achesnovitskiy.octocattest2.extensions.showKeyboard
import com.achesnovitskiy.octocattest2.ui.base.BaseFragment
import com.achesnovitskiy.octocattest2.ui.repos.di.DaggerReposComponent
import com.achesnovitskiy.octocattest2.ui.repos.di.ReposModule
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_repos.*
import javax.inject.Inject

class ReposFragment : BaseFragment(R.layout.fragment_repos) {

    @Inject
    lateinit var reposViewModel: ReposViewModel

    private val reposAdapter: ReposAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReposAdapter { repo ->
            this.findNavController()
                .navigate(
                    ReposFragmentDirections.actionRepositoriesFragmentToRepoInfoFragment(repo.name)
                )
        }
    }

    private val snackbar: Snackbar by lazy(LazyThreadSafetyMode.NONE) {
        Snackbar.make(
            requireView(),
            "",
            Snackbar.LENGTH_SHORT
        )
    }

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
        // START Set progress bar color for pre-lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawableProgress =
                DrawableCompat.wrap(repos_progress_bar.indeterminateDrawable)

            DrawableCompat.setTint(
                drawableProgress,
                ContextCompat.getColor(requireActivity(), R.color.color_accent)
            )

            repos_progress_bar.indeterminateDrawable = DrawableCompat.unwrap(drawableProgress)
        }
        // END

        // START Setup recycler view
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        with(repos_recycler_view) {
            adapter = reposAdapter

            layoutManager = LinearLayoutManager(context)

            addItemDecoration(divider)
        }

        repos_swipe_refresh_layout.setOnRefreshListener {
            reposViewModel.refreshObserver.onNext(Unit)

            repos_swipe_refresh_layout.isRefreshing = false
        }
        // END

        // START Setup search view
        repos_search_button.setOnClickListener {
            reposViewModel.searchToggleObserver.onNext(true)
        }

        repos_search_back_button.setOnClickListener {
            reposViewModel.searchToggleObserver.onNext(false)
        }

        repos_search_close_button.setOnClickListener {
            repos_search_edit_text.text = null
        }

        repos_search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                repos_search_close_button.isEnabled = !text.isNullOrEmpty()

                reposViewModel.searchQueryObserver.onNext(text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
        // END
    }

    override fun onResume() {
        super.onResume()

        disposable = CompositeDisposable(
            reposViewModel.reposIsSearchObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isSearch ->
                    repos_search_layout.isVisible = isSearch
                    repos_search_button.isVisible = !isSearch
                    repos_title.isVisible = !isSearch

                    if (isSearch) {
                        repos_search_edit_text.requestFocus()

                        requireActivity().showKeyboard()
                    } else {
                        repos_search_edit_text.setText("")

                        requireActivity().hideKeyboard()
                    }
                },

            reposViewModel.reposWithSearchObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { repos ->
                    repos_list_is_empty_text_view.isVisible = repos.isNullOrEmpty()

                    reposAdapter.submitList(repos.sortedBy { it.name })
                },

            reposViewModel.loadingStateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ loadingState ->
                    repos_progress_bar.isVisible = loadingState.isLoading

                    Log.d("My_ReposFragment", "$loadingState")

//                    if (loadingState.errorRes != null) {
//                        snackbar
//                            .setText(loadingState.errorRes)
//                            .setDuration(Snackbar.LENGTH_INDEFINITE)
//                            .setAction(getString(R.string.repeat)) {
//                                reposViewModel.refreshObserver.onNext(Unit)
//                            }
//                            .show()
//
//                        Log.d("My_ReposFragment", "Show snackbar")
//                    } else {
//                        if (snackbar.isShown) {
//                            snackbar.dismiss()
//
//                            Log.d("My_ReposFragment", "Hide snackbar")
//                        }
//                    }
                    loadingState.errorRes?.let {
                        Snackbar.make(
                            requireView(),
                            getString(it),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        )
    }
}