package com.sunrisesunset

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    lateinit var appContext: Context

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        activityTestRule.finishActivity()
    }

    @Test
    fun goThroughTest() {
        val notifiedText = appContext.getString(R.string.searchNotFound)

        if (doesViewExist(R.id.buttonSearch)) {
            val signInButton = onView(withId(R.id.buttonSearch)).check(matches(isDisplayed()))
            signInButton.perform(click())

            val editText =
                onView(
                    allOf(
                        withId(R.id.editTextSearch),
                        withTagValue(`is`("tagEditText" as Any))
                    )
                )

            editText.perform(typeText(STRING_TO_BE_TYPED))

            onView(withId(R.id.buttonSearch)).check(matches(isDisplayed())).perform(click())
            Thread.sleep(1000)


            editText.perform(replaceText(STRING_TO_BE_TYPED2))
            onView(withId(R.id.buttonSearch)).check(matches(isDisplayed())).perform(click())
            Thread.sleep(1000)

            onView(withId(R.id.textViewNotification)).check(matches(withText(notifiedText)))

            editText.perform(replaceText(STRING_TO_BE_TYPED3))
            onView(withId(R.id.buttonSearch)).check(matches(isDisplayed())).perform(click())
            Thread.sleep(2000)

            //androidx.test.espresso.PerformException: Error performing 'click drawable ' on view 'Animations or transitions are enabled on the target device.
            //need to turn of Transition in Developer options of phone
            //onView(withId(R.id.textInputLayoutSearch)).check(matches(isDisplayed())).perform(ClickDrawableAction(ClickDrawableAction.Right))

            editText.perform(replaceText(STRING_TO_BE_TYPED4), closeSoftKeyboard())
            onView(withId(R.id.buttonSearch)).check(matches(isDisplayed())).perform(click())
            Thread.sleep(3000)
        }
    }

    fun doesViewExist(id: Int): Boolean {
        return try {
            onView(withId(id)).check(matches(isDisplayed()))
            true
        } catch (e: NoMatchingViewException) {
            false
        }
    }

    companion object {
        val STRING_TO_BE_TYPED = "Ankara"
        val STRING_TO_BE_TYPED2 = "zzzretevrvrfervrtvevtar"
        val STRING_TO_BE_TYPED3 = "Cairo"
        val STRING_TO_BE_TYPED4 = "Tallinn"
        val TAG = "EspressoTest"
    }

}