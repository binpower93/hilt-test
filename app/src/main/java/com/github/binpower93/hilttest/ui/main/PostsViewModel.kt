package com.github.binpower93.hilttest.ui.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.github.binpower93.hilttest.core.BaseViewModel
import com.github.binpower93.hilttest.model.network.PostApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsViewModel : BaseViewModel() {
    @Inject lateinit var postApi: PostApi
    private lateinit var subscription: Disposable

    val loadingVisibility = MutableLiveData<Int>()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        subscription = postApi.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { onRetrievePostListSuccess() },
                { onRetrievePostListError() }
            )
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess() {

    }

    private fun onRetrievePostListError() {

    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}