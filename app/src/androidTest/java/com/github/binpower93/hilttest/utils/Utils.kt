package com.github.binpower93.hilttest.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.remote.annotation.RemoteMsgConstructor
import androidx.test.espresso.remote.annotation.RemoteMsgField
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers


/**
 * Perform action of waiting for a specific time.
 */
fun waitFor(millis: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
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

fun hasNoErrorText(): Matcher<View?>? {
    return object : BoundedMatcher<View?, TextInputLayout>(TextInputLayout::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has no error text: ")
        }

        override fun matchesSafely(view: TextInputLayout): Boolean {
            return view.error == null
        }
    }
}


/**
 * Returns a matcher that matches [EditText] based on edit text error string value.
 *
 *
 * **Note:** View's error property can be `null`, to match against it use `
 * hasErrorText(nullValue(String.class)`
 */
fun hasErrorText(stringMatcher: Matcher<String>?): Matcher<View?>? {
    return HasErrorTextMatcher(checkNotNull(stringMatcher))
}

/**
 * Returns a matcher that matches [EditText] based on edit text error string value.
 *
 *
 * Note: Sugar for `hasErrorText(is("string"))`.
 */
fun hasErrorText(expectedError: String): Matcher<View?>? {
    return hasErrorText(Matchers.`is`(expectedError))
}

internal class HasErrorTextMatcher @RemoteMsgConstructor constructor(
    @field:RemoteMsgField(
        order = 0
    ) private val stringMatcher: Matcher<String>
) :
    BoundedMatcher<View?, TextInputLayout>(TextInputLayout::class.java) {
    override fun describeTo(description: Description) {
        description.appendText("with error: ")
        stringMatcher.describeTo(description)
    }

    override fun matchesSafely(view: TextInputLayout): Boolean {
        return stringMatcher.matches(view.error)
    }

}