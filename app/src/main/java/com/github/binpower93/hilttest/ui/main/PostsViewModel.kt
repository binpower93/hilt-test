package com.github.binpower93.hilttest.ui.main

import androidx.lifecycle.ViewModel
import com.github.binpower93.hilttest.core.BaseViewModel
import com.github.binpower93.hilttest.model.network.PostApi
import javax.inject.Inject

class PostsViewModel : BaseViewModel() {
    @Inject lateinit var postApi: PostApi
}