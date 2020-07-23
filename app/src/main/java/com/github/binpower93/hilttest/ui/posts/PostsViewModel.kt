package com.github.binpower93.hilttest.ui.posts

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
    private lateinit var subscription: Disposable

    val loadingVisibility = MutableLiveData<Int>()
    val errorMessage = MutableLiveData<Int>()

    val errorRetryListener = View.OnClickListener { loadPosts() }
    val postsAdapter = PostsAdapter()

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
                this::onRetrievePostListSuccess
            ) { onRetrievePostListError() }
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(posts: List<Post?>) {
        postsAdapter.update(posts.filterNotNull())
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.posts_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}