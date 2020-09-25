package com.sunrisesunset.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.sunrisesunset.MainActivity.Companion.mainActivity

class LocationUtil(context: Context, locationCallback: LocationCallback) {

    private var ctx: Context
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationManager: LocationManager? = null

    private val INTERVAL: Long = 10000
    private val FASTEST_INTERVAL: Long = 9000
    var lastLocation: Location? = null
    private var locationRequest: LocationRequest
    private var locCallback: LocationCallback

    init {
        ctx = context
        locCallback = locationCallback
        locationRequest = LocationRequest()
        locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
    }

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    mainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    mainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

        // Create the location request to start receiving updates
        locationRequest = LocationRequest()
        locationRequest.setInterval(INTERVAL)
        locationRequest.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(mainActivity)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locCallback, Looper.myLooper()
        )
    }

    fun stoplocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locCallback)
    }

}