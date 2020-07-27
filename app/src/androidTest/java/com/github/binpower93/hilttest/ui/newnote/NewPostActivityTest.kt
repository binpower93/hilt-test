package com.github.binpower93.hilttest.ui.newnote

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.utils.waitFor
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class NewNoteActivityTest {

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule<NewNoteActivity>(NewNoteActivity::class.java)

    @Test
    fun testThatInitialStateHasNoTitleNoContentAndCannotBeSaved() {
        onView(withId(R.id.noteTitleText)).check(matches(withText("")))
        onView(withId(R.id.noteContentText)).check(matches(withText("")))
        onView(withId(R.id.save)).check(matches(not(isEnabled())))
    }

    @Test
    fun testsThatEmptyTitleAndContentDisablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText(""))
        onView(withId(R.id.noteContentText)).perform(replaceText(""))

        onView(withId(R.id.save)).check(matches(not(isEnabled())))

        onView(withId(R.id.noteTitle)).check(
            matches(
                hasErrorText(
                    mActivityRule.activity.getString(R.string.error_title_needed)
                )
            )
        )

        onView(withId(R.id.noteContent)).check(
            matches(
                hasErrorText(
                    mActivityRule.activity.getString(R.string.error_content_needed)
                )
            )
        )
    }

    @Test
    fun testsThatEmptyTitleButValidContentDisablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText(""))
        onView(withId(R.id.noteContentText)).perform(replaceText("Valid Text"))

        onView(withId(R.id.save)).check(matches(not(isEnabled())))

        onView(withId(R.id.noteTitle)).check(
            matches(
                hasErrorText(
                    mActivityRule.activity.getString(R.string.error_content_needed)
                )
            )
        )

        onView(withId(R.id.noteContent)).check(matches(hasErrorText("")))
    }

    @Test
    fun testsThatEmptyContentButValidTitleDisablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText("Valid Text"))
        onView(withId(R.id.noteContentText)).perform(replaceText(""))

        onView(withId(R.id.save)).check(matches(not(isEnabled())))
        onView(withId(R.id.noteTitle)).check(matches(hasErrorText("")))

        onView(withId(R.id.noteContent)).check(
            matches(
                hasErrorText(
                    mActivityRule.activity.getString(R.string.error_title_needed)
                )
            )
        )
    }


    @Test
    fun testsThatValidTitleAndContentEnablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText("Valid Text"))
        onView(withId(R.id.noteContentText)).perform(replaceText("Valid Text"))

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.save)).check(matches(isEnabled()))
        onView(withId(R.id.noteTitle)).check(matches(hasErrorText("")))
        onView(withId(R.id.noteContent)).check(matches(hasErrorText("")))
    }
}