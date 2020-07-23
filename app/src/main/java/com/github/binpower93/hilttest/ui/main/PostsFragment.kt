package com.github.binpower93.hilttest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.binpower93.hilttest.databinding.PostsFragmentBinding

class PostsFragment : Fragment() {
    private lateinit var binding: PostsFragmentBinding
    private lateinit var viewModel: PostsViewModel

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
    }

    companion object {
        fun newInstance() = PostsFragment()
    }
}