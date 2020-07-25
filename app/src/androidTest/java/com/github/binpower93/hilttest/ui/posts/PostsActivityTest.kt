package com.github.binpower93.hilttest.ui.posts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.model.Post
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PostsActivityTest {

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule<PostsActivity>(PostsActivity::class.java)

    @Test
    fun testThatPassing3PostsShows3PostsWithTheCorrectValues() {
        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListSuccess(
                listOf(
                    Post(
                        id = "item-1",
                        title = "Item 1",
                        content = "Item 1's Description"
                    ),
                    Post(
                        id = "item-2",
                        title = "Item 2",
                        content = "Item 2's Description"
                    ),
                    Post(
                        id = "item-3",
                        title = "Item 3",
                        content = "Item 3's Description"
                    )
                )
            )
        }

        onView(withId(R.id.posts)).check(
            matches(
                allOf(
                    isCompletelyDisplayed(),
                    hasChildCount(3)
                )
            )
        )
    }

    @Test
    fun testThatWhenToggleTheLoadingIndicatorTheProgressBarBehavesAsExpected() {
        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListStart()
        }

        onView(withId(R.id.posts)).check(matches(isDisplayed()))
        onView(withId(R.id.progress)).check(matches(isCompletelyDisplayed()))

        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListFinish()
        }

        onView(withId(R.id.posts)).check(matches(isDisplayed()))
        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testThatSnackBarShowsErrorWhenErrorIsShown() {
        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListError()
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.posts_error)))

        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.clearErrorMessage()
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(doesNotExist())
    }
}
