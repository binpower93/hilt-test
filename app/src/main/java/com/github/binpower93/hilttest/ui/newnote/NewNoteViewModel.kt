package com.github.binpower93.hilttest.ui.newnote

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.core.BaseViewModel
import com.github.binpower93.hilttest.model.Post
import com.github.binpower93.hilttest.model.network.PostApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.combine
import java.util.*
import javax.inject.Inject

class NewNoteViewModel: BaseViewModel() {
    @Inject lateinit var postApi: PostApi

    val savingVisibility = MutableLiveData<Int>()
    val saveVisibility = savingVisibility.map { if(it == View.GONE) View.VISIBLE else View.GONE }
    val errorMessage = MutableLiveData<Int>()
    val success = MutableLiveData<Unit>()

    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()

    private var subscription: Disposable? = null

    val titleError = title.map {
        if(it.isNullOrBlank()) {
            R.string.error_title_needed
        } else {
            null
        }
    }

    val contentError = content.map {
        if(it.isNullOrBlank()) {
            R.string.error_content_needed
        } else {
            null
        }
    }

    val canSubmit = combine(titleError.asFlow(), contentError.asFlow()) { titleError, contentError ->
        titleError == null && contentError == null
    }.asLiveData()

    fun submit() {
        if(subscription?.isDisposed == false) {
            subscription?.dispose()
        }

        subscription = postApi.insertPost(
            Post(
                id = UUID.randomUUID().toString(),
                title = title.value.orEmpty(),
                content = content.value.orEmpty()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onSaveStarted() }
            .doOnTerminate { onSaveFinished() }
            .subscribe(
                { onSaveSuccess() },
                { onSaveFailed() }
            )
    }

    init {
        savingVisibility.value = View.GONE
    }

    fun onSaveSuccess() {
        success.value = Unit
    }

    fun onSaveFailed() {
        errorMessage.value = R.string.error_failed_to_save
    }

    fun onSaveFinished() {
        savingVisibility.value = View.GONE
    }

    fun onSaveStarted() {
        savingVisibility.value = View.VISIBLE
        clearErrorMessage()
    }

    fun clearErrorMessage() {
        errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}