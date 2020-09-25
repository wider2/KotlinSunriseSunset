package com.sunrisesunset.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class Utilities {

    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun timeToZoneTime(strDate: String, zoneId: String): String {
        val timeZone = ZoneId.of(zoneId)
        // use with standard: yyyy-MM-dd'T'HH:mm:ssZ, by example 2020-09-24T04:12:18+00:00
        val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val resultOfParsing = OffsetDateTime.parse(strDate, dateFormatter)

        val remoteLocationTime: ZonedDateTime = resultOfParsing.atZoneSameInstant(timeZone)
        // let's show time only for UI
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        return timeFormat.format(remoteLocationTime)
    }

    fun timeModulusPair(totalSecs: Int): Pair<Int, Int> {
        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60

        return Pair(hours, minutes)
    }

    @SuppressLint("SimpleDateFormat")
    fun todayDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("MMM dd, yyyy")
        return formatter.format(date)
    }

}