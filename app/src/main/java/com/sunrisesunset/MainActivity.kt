package com.sunrisesunset

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sunrisesunset.fragments.BaseFragment
import com.sunrisesunset.fragments.SunFragment
import com.sunrisesunset.utils.*
import android.provider.Settings
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var snackBar: Snackbar? = null
    private val networkUtil = NetworkUtil(this)
    private var selectedFragment: BaseFragment? = null
    private lateinit var locationUtil: LocationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this

        locationUtil = LocationUtil(baseContext, locationCallback)

        if (savedInstanceState == null) {
            val currentFragment = addFragmentSafely(
                fragment = SunFragment(),
                tag = SunFragment::class.java.simpleName,
                containerViewId = R.id.fragmentContainer,
                allowStateLoss = true
            )
            selectedFragment = currentFragment
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()
        networkUtil.register()
        //check for network connection before Android 8
        registerReceiver(
            ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        ConnectivityReceiver.connectivityReceiverListener = this

        locationUtil.lastLocation = null
        locationUtil.checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        networkUtil.unregister()
        locationUtil.stoplocationUpdates()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnackBarMessage(isConnected)
    }

    private fun showSnackBarMessage(isConnected: Boolean) {
        snackBar = Snackbar.make(textViewTop, "", Snackbar.LENGTH_INDEFINITE)
        if (!isConnected) {
            snackBar = Snackbar
                .make(textViewTop, getString(R.string.noConnection), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(ContextCompat.getColor(baseContext, R.color.colorYellow))
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.networkSettings), object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val intent = Intent(Settings.ACTION_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                })

            val sbView = snackBar?.getView()
            val textView =
                sbView?.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            snackBar?.show()
        } else {
            snackBar?.dismiss()
        }
    }

    //listener waits for enabled service
    //region LocationListener
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        if (locationUtil.lastLocation == null) {
            if (Utilities().isNetworkAvailable(baseContext))
                selectedFragment?.updateMyCoordinates(location.latitude, location.longitude)
        }
        locationUtil.lastLocation = location
    }
    //endregion

    companion object {
        private const val TAG = "MainActivity"
        lateinit var mainActivity: MainActivity
    }
}