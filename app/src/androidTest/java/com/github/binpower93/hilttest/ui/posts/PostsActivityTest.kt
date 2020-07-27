package com.github.binpower93.hilttest.ui.posts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.model.Post
import org.hamcrest.Description
import org.hamcrest.Matcher
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

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.posts)).check(
            matches(
                allOf(
                    isCompletelyDisplayed(),
                    hasChildCount(3),
                    atPosition(0,
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
                    ),
                    atPosition(1,
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
                    ),
                    atPosition(2,
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

        onView(isRoot()).perform(waitFor(500))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(doesNotExist())
    }
}

/**
 * Perform action of waiting for a specific time.
 */
fun waitFor(millis: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "Wait for $millis milliseconds."
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(millis)
        }
    }
}

fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}