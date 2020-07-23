package com.github.binpower93.hilttest.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.databinding.PostsFragmentBinding
import com.google.android.material.snackbar.Snackbar

class PostsFragment : Fragment() {
    private lateinit var binding: PostsFragmentBinding
    private lateinit var viewModel: PostsViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = PostsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostsViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorResource ->
            if(errorResource != null) {
                showError(errorResource)
            } else {
                hideError()
            }
        })
    }

    private fun showError(@StringRes errorRes: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorRes, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.common_retry, viewModel.errorRetryListener)
            .apply { show() }
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    companion object {
        fun newInstance() = PostsFragment()
    }
}