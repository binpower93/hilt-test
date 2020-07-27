package com.github.binpower93.hilttest.ui.newnote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.core.BaseViewModel
import kotlinx.coroutines.flow.combine

class NewNoteViewModel: BaseViewModel() {

    val title = MutableLiveData<String>()
    val content = MutableLiveData<String>()

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


}