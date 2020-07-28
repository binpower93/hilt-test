package com.github.binpower93.hilttest.ui.posts

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.core.BaseViewModel
import com.github.binpower93.hilttest.model.Post
import com.github.binpower93.hilttest.model.network.PostApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsViewModel : BaseViewModel() {
    @Inject lateinit var postApi: PostApi
    private var subscription: Disposable? = null

    val loadingVisibility = MutableLiveData<Int>()
    val errorMessage = MutableLiveData<Int>()
    val emptyStateVisibility = MutableLiveData<Int>()

    val errorRetryListener = View.OnClickListener { loadPosts() }
    val postsAdapter = PostsAdapter()

    fun loadPosts() {
        Log.d("PVM", "loadPosts")

        if(subscription?.isDisposed == false) {
            subscription?.dispose()
        }

        subscription = postApi.getPosts()
            .subscribeOn(Schedulers.io())
            .map { it.values.toList() }
            .doOnError { onRetrievePostListError() }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                this::onRetrievePostListSuccess
            ) { onRetrievePostListError() }
    }

    fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        clearErrorMessage()
    }

    fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    fun onRetrievePostListSuccess(posts: List<Post?>) {
        val newData = posts.filterNotNull()
        postsAdapter.update(newData)
        emptyStateVisibility.value = if(newData.isEmpty()) View.VISIBLE else View.GONE
    }

    fun onRetrievePostListError() {
        errorMessage.value = R.string.posts_error
    }

    fun clearErrorMessage() {
        errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}