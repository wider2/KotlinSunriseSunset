package com.sunrisesunset

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class ActivityTest {

    lateinit var scenario: ActivityScenario<MainActivity>
    lateinit var appContext: Context

    @Before
    fun setup() {
        scenario = launchActivity<MainActivity>()
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun testScenario() {
        if (::scenario.isInitialized) {
            scenario.moveToState(Lifecycle.State.CREATED)
            scenario.onActivity { activity ->
                assertTrue(activity.textViewTop.text.equals(""))
            }
            scenario.moveToState(Lifecycle.State.RESUMED)
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }

}