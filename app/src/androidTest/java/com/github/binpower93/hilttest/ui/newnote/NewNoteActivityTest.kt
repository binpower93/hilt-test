package com.github.binpower93.hilttest.ui.newnote

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.github.binpower93.hilttest.R
import com.github.binpower93.hilttest.utils.hasNoErrorText
import com.github.binpower93.hilttest.utils.waitFor
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.github.binpower93.hilttest.utils.hasErrorText as hasTILErrorText


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
        onView(withId(R.id.save)).check(
            matches(
                allOf(
                    isCompletelyDisplayed(),
                    not(isEnabled())
                )
            )
        )
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testsThatEmptyTitleAndContentDisablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText(""))
        onView(withId(R.id.noteContentText)).perform(replaceText(""))

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.save)).check(matches(not(isEnabled())))

        onView(withId(R.id.noteTitle)).check(
            matches(
                hasTILErrorText(
                    mActivityRule.activity.getString(R.string.error_title_needed)
                )
            )
        )

        onView(withId(R.id.noteContent)).check(
            matches(
                hasTILErrorText(
                    mActivityRule.activity.getString(R.string.error_content_needed)
                )
            )
        )
    }

    @Test
    fun testsThatEmptyTitleButValidContentDisablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText(""))
        onView(withId(R.id.noteContentText)).perform(replaceText("Valid Text"))

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.save)).check(matches(not(isEnabled())))

        onView(withId(R.id.noteTitle)).check(
            matches(
                hasTILErrorText(
                    mActivityRule.activity.getString(R.string.error_title_needed)
                )
            )
        )

        onView(withId(R.id.noteContent)).check(matches(hasNoErrorText()))
    }

    @Test
    fun testsThatEmptyContentButValidTitleDisablesSaveButton() {
        onView(withId(R.id.noteTitleText)).perform(replaceText("Valid Text"))
        onView(withId(R.id.noteContentText)).perform(replaceText(""))

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.save)).check(matches(not(isEnabled())))
        onView(withId(R.id.noteTitle)).check(matches(hasNoErrorText()))

        onView(withId(R.id.noteContent)).check(
            matches(
                hasTILErrorText(
                    mActivityRule.activity.getString(R.string.error_content_needed)
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

        onView(withId(R.id.noteTitle)).check(matches(hasNoErrorText()))

        onView(withId(R.id.noteContent)).check(matches(hasNoErrorText()))
    }

    @Test
    fun testThatSavingChangesSaveButtonAndProgressIndicatorAsExpected() {
        mActivityRule.activity.runOnUiThread {
            mActivityRule.activity.viewModel.onSaveStarted()
        }

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.progressBar)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.save)).check(matches(not(isDisplayed())))

        mActivityRule.activity.runOnUiThread {
            mActivityRule.activity.viewModel.onSaveFinished()
        }

        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.save)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testThatErrorDisplaysExpectedAndDisappearsAfter5Seconds() {
        mActivityRule.activity.runOnUiThread {
            mActivityRule.activity.viewModel.onSaveFailed()
        }

        onView(isRoot()).perform(waitFor(100))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.error_failed_to_save)))

        onView(isRoot()).perform(waitFor(5000))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testThatErrorDisplaysExpectedAndDisappearsAfterErrorMessageIsCleared() {
        mActivityRule.activity.runOnUiThread {
            mActivityRule.activity.viewModel.onSaveFailed()
        }

        onView(isRoot()).perform(waitFor(100))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.error_failed_to_save)))

        mActivityRule.activity.runOnUiThread {
            mActivityRule.activity.viewModel.clearErrorMessage()
        }

        onView(isRoot()).perform(waitFor(250))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.doesNotExist())
    }
}