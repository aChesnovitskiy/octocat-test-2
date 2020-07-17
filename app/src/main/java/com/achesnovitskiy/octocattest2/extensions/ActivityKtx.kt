package com.achesnovitskiy.octocattest2.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard() {
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        .toggleSoftInput(
            InputMethodManager.SHOW_IMPLICIT,
            0
        )
}

fun Activity.hideKeyboard() {
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(
            window.decorView.windowToken,
            0
        )
}