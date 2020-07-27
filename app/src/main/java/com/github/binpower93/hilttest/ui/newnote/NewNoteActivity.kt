package com.github.binpower93.hilttest.ui.newnote

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.databinding.NewNoteActivityBinding
import com.google.android.material.snackbar.Snackbar

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: NewNoteActivityBinding
    private lateinit var viewModel: NewNoteViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note_activity)

        binding = DataBindingUtil.setContentView(this, R.layout.new_note_activity)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(NewNoteViewModel::class.java)
        binding.viewModel = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.errorMessage.observe(this, Observer { errorResource ->
            if(errorResource != null) {
                showError(errorResource)
            } else {
                hideError()
            }
        })

        viewModel.success.observe(this, Observer {
            if(!isFinishing) finish()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showError(@StringRes errorRes: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorRes, Snackbar.LENGTH_SHORT)
            .apply { show() }
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}