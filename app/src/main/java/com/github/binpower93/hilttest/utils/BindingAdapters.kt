package com.github.binpower93.hilttest.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: Int?) {
    view.error = errorMessage?.let { view.resources.getString(it) }
}