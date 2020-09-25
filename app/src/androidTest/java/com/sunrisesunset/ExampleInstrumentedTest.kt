package com.sunrisesunset

import androidx.fragment.app.Fragment
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.sunrisesunset.fragments.SunFragment
import com.sunrisesunset.utils.addFragmentSafely
import com.sunrisesunset.utils.replaceFragmentSafely
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java, true, true)

    @After
    fun tearDown() {
        activityTestRule.finishActivity()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sunrisesunset", appContext.packageName)
    }

    //check extension methods
    @Test
    fun addFragment() {
        val testFragment: Fragment = activityTestRule.activity.addFragmentSafely(
            fragment = SunFragment(),
            tag = SunFragment::class.java.simpleName,
            containerViewId = R.id.fragmentContainer,
            allowStateLoss = true
        )
        assertNotNull(testFragment)
        assertTrue(testFragment.isAdded)
    }

    @Test
    fun replaceFragment() {
        val testFragment = activityTestRule.activity.replaceFragmentSafely(
            fragment = SunFragment(),
            tag = SunFragment::class.java.simpleName,
            containerViewId = R.id.fragmentContainer,
            allowStateLoss = true
        )
        assertNotNull(testFragment)
    }
}