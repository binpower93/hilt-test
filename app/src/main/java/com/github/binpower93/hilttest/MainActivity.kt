package com.github.binpower93.hilttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.binpower93.hilttest.ui.posts.PostsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PostsFragment.newInstance())
                    .commitNow()
        }
    }
}