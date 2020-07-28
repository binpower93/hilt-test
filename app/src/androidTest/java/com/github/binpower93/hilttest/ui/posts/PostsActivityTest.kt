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
import com.github.binpower93.hilttest.utils.atPosition
import com.github.binpower93.hilttest.utils.waitFor
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
        // Wait for the network to complete with auto loading feed
        onView(isRoot()).perform(waitFor(1000))

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

        onView(isRoot()).perform(waitFor(500))

        onView(withId(R.id.emptyState)).check(matches(not(isDisplayed())))

        onView(withId(R.id.posts))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(hasChildCount(3)))
            .check(
                matches(
                    atPosition(
                        0,
                        allOf(
                            hasDescendant(
                                allOf(
                                    withId(R.id.post_title),
                                    withText("Item 1")
                                )
                            ),
                            hasDescendant(
                                allOf(
                                    withId(R.id.post_content),
                                    withText("Item 1's Description")
                                )
                            )
                        )
                    )
                )
            )
            .check(
                matches(
                    atPosition(
                        1,
                        allOf(
                            hasDescendant(
                                allOf(
                                    withId(R.id.post_title),
                                    withText("Item 2")
                                )
                            ),
                            hasDescendant(
                                allOf(
                                    withId(R.id.post_content),
                                    withText("Item 2's Description")
                                )
                            )
                        )
                    )
                )
            )
            .check(
                matches(
                    atPosition(
                        2,
                        allOf(
                            hasDescendant(
                                allOf(
                                    withId(R.id.post_title),
                                    withText("Item 3")
                                )
                            ),
                            hasDescendant(
                                allOf(
                                    withId(R.id.post_content),
                                    withText("Item 3's Description")
                                )
                            )
                        )
                    )
                )
            )
    }

    @Test
    fun testThatWhenToggleTheLoadingIndicatorTheProgressBarBehavesAsExpected() {
        // Wait for the network to complete with auto loading feed
        onView(isRoot()).perform(waitFor(1000))

        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListStart()
        }

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.posts)).check(matches(isDisplayed()))
        onView(withId(R.id.progress)).check(matches(isCompletelyDisplayed()))

        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListFinish()
        }

        onView(isRoot()).perform(waitFor(100))

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

        onView(isRoot()).perform(waitFor(250))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(doesNotExist())
    }
    @Test
    fun testThatPassingNoPostsShowsEmptyState() {
        // Wait for the network to complete with auto loading feed
        onView(isRoot()).perform(waitFor(1000))

        mActivityRule.runOnUiThread {
            mActivityRule.activity.viewModel.onRetrievePostListSuccess(listOf())
        }

        onView(isRoot()).perform(waitFor(500))

        onView(withId(R.id.emptyState)).check(matches(isDisplayed()))

        onView(withId(R.id.posts))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(hasChildCount(0)))
    }
}