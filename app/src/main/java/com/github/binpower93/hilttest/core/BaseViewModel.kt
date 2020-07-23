package com.github.binpower93.hilttest.core

import androidx.lifecycle.ViewModel
import com.github.binpower93.hilttest.di.DaggerViewModelInjector
import com.github.binpower93.hilttest.di.NetworkModule
import com.github.binpower93.hilttest.ui.posts.PostsViewModel

abstract class BaseViewModel: ViewModel() {
    private val injector = DaggerViewModelInjector.builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when(this) {
            is PostsViewModel -> injector.inject(this)
        }
    }
}