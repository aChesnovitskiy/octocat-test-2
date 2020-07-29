package com.achesnovitskiy.octocattest2.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposable

abstract class BaseFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId) {

    open var disposable: Disposable? = null

    override fun onPause() {
        disposable?.dispose()

        super.onPause()
    }

    override fun onDestroy() {
        disposable?.dispose()

        super.onDestroy()
    }
}