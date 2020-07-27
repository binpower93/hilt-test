package com.github.binpower93.hilttest.ui.newnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.databinding.NewNoteActivityBinding

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: NewNoteActivityBinding
    private lateinit var viewModel: NewNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note_activity)

        binding = DataBindingUtil.setContentView(this, R.layout.new_note_activity)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(NewNoteViewModel::class.java)
        binding.viewModel = viewModel
    }
}