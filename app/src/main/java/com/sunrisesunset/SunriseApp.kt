package com.sunrisesunset

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sunrisesunset.data.dao.SunDatabase

class SunriseApp : Application() {
    lateinit var sunDatabase : SunDatabase

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        sunriseApp = this
        sunDatabase = SunDatabase.invoke(applicationContext)
        val isDbOpened: Boolean = sunDatabase.isOpen

        Log.d(TAG,"Room db: " + sunDatabase.isOpen)
        if (BuildConfig.DEBUG && isDbOpened) {
            sunDatabase.clearAllTables()
        }

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
    }

    companion object {
        lateinit var sunriseApp: SunriseApp
        var TAG: String = SunriseApp::class.java.simpleName
    }
}