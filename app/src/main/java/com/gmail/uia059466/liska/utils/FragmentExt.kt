package com.gmail.uia059466.liska.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard() {
    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}


fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}
