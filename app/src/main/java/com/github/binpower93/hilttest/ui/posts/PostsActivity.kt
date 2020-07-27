package com.github.binpower93.hilttest.ui.posts

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.databinding.PostsActivityBinding
import com.github.binpower93.hilttest.ui.newnote.NewNoteActivity
import com.google.android.material.snackbar.Snackbar

class PostsActivity : AppCompatActivity() {

    private lateinit var binding: PostsActivityBinding
    lateinit var viewModel: PostsViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.posts_activity)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(PostsViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.errorMessage.observe(this, Observer { errorResource ->
            if(errorResource != null) {
                showError(errorResource)
            } else {
                hideError()
            }
        })

        binding.newNote.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NewNoteActivity::class.java
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadPosts()
    }

    private fun showError(@StringRes errorRes: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorRes, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.common_retry, viewModel.errorRetryListener)
            .apply { show() }
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

}