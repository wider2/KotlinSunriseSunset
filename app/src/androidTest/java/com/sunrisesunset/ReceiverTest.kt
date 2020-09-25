package com.sunrisesunset

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sunrisesunset.SunriseApp.Companion.TAG
import com.sunrisesunset.utils.NetworkUtil
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ReceiverTest : BroadcastReceiver() {

    lateinit var appContext: Context

    override fun onReceive(ctx: Context, i: Intent) {
        isNetworkAvailable()
    }

    @Before
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.sunrisesunset", appContext.packageName)
    }

    @Test
    fun isNetworkAvailable() {
        var isConnected = false

        val connectivity = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            for (j in info.indices) {
                if (info[j].state == NetworkInfo.State.CONNECTED) {
                    if (!isConnected) {
                        Log.v(TAG, "Now you are connected to Internet")
                        isConnected = true
                    }
                }
            }
        }
        Log.v(TAG, "You are not connected to Internet")

        assertTrue(isConnected)
    }

    @Test
    fun broadcastTest() {
        val networkChangeReceiver = NetworkUtil(appContext).networkChangeReceiver
        assertNotNull(networkChangeReceiver.onReceive(appContext, Intent()))
    }

}