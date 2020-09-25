package com.sunrisesunset.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.sunrisesunset.EventObserver
import com.sunrisesunset.R
import com.sunrisesunset.model.NominatimResponse
import com.sunrisesunset.model.SunResponse
import com.sunrisesunset.utils.TimezoneMapper
import com.sunrisesunset.utils.Utilities
import com.sunrisesunset.utils.hideKeyboard
import com.sunrisesunset.utils.showKeyboard
import com.sunrisesunset.viewmodel.NominatimViewModel
import com.sunrisesunset.viewmodel.NominatimViewModelFactory
import com.sunrisesunset.viewmodel.SunViewModel
import com.sunrisesunset.viewmodel.SunViewModelFactory
import kotlinx.android.synthetic.main.fragment_info.*

import java.util.*

class SunFragment : BaseFragment() {
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationName = ""
    private var showBySearch = false
    private var currentTimeZone = ""

    val viewModel: SunViewModel by viewModels {
        SunViewModelFactory()
    }

    val nominatimViewModel: NominatimViewModel by viewModels {
        NominatimViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        clickListener()
    }

    //get GPS data from activity
    override fun updateMyCoordinates(lat: Double?, lng: Double?) {
        if (textInputLayoutSearch?.editText?.text.toString().length == 0) showBySearch = false

        if (lat == null || lng == null) {
            textViewNotification?.text = getString(R.string.noLocation)
        } else if (!showBySearch) {
            locationName = ""
            showByRequest(lat, lng)
        }
    }
    private fun showByRequest(lat: Double?, lng: Double?) {
        val nullableLat: Double? = lat
        latitude = nullableLat ?: 0.0
        val nullableLng: Double? = lng
        longitude = nullableLng ?: 0.0

        progressBar?.visibility = View.VISIBLE
        relativeLayoutSun?.visibility = View.GONE

        // using external library to get timezone by coordinate
        currentTimeZone = TimezoneMapper.latLngToTimezoneString(latitude, longitude)
        Log.d(TAG, currentTimeZone)

        if (locationName.equals("") && context != null) { //get name by coordinates
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(lat!!, lng!!, 1)
            if (!addresses.isEmpty() && addresses.size > 0) {
                val item = addresses.get(0)
                locationName = getString(R.string.locationPlace, item.countryName, item.locality, item.thoroughfare, item.subThoroughfare)
            }
        }
        viewModel.getSunDetailsByCoordinates(lat, lng)
    }

    override fun coordinatesAbsent(error: String) {
        textViewNotification?.text = error
    }

    //get Sun data from Api endpoint
    private fun setupObservers() {
        viewModel.showItemsError.observe(viewLifecycleOwner, EventObserver {
            showError(it)
        })
        viewModel.sunResponse.observe(viewLifecycleOwner, EventObserver {
            showSunDetails(it)
        })

        nominatimViewModel.showItemsError.observe(viewLifecycleOwner, EventObserver {
            showError(it)
        })
        nominatimViewModel.nominatimResponse.observe(viewLifecycleOwner, EventObserver {
            parseNominatim(it)
        })
    }

    private fun showError(error: String) {
        hideDetails()
        textViewNotification?.text = error
    }
    private fun hideDetails() {
        textViewNotification?.text = ""
        relativeLayoutSun?.visibility = View.GONE
        textViewPlace?.text = ""
        textViewLocation?.text = ""
        progressBar.visibility = View.GONE
    }

    //region UIview
    private fun clickListener() {
        buttonSearch?.setOnClickListener {
            val inputText = textInputLayoutSearch?.editText?.text.toString()
            if (inputText.length < 2) {
                textInputLayoutSearch.setError(getString(R.string.noInputFound))
            } else {
                showBySearch = true
                hideDetails()
                Log.wtf(TAG, inputText)
                textInputLayoutSearch?.hideKeyboard()
                textInputLayoutSearch.setError(null)
                nominatimViewModel.getCoordinatesByCity(inputText)
            }
        }
        editTextSearch?.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                buttonSearch?.performClick()
                true
            } else {
                false
            }
        })
        textInputLayoutSearch?.setEndIconOnClickListener {
            textInputLayoutSearch?.showKeyboard()
            textViewNotification?.text = ""
            textInputLayoutSearch?.editText?.setText("")
            showBySearch = false
        }
    }

    private fun showSunDetails(sunResponse: SunResponse) {
        textViewNotification?.text = ""
        progressBar.visibility = View.GONE
        relativeLayoutSun?.visibility = View.VISIBLE
        textViewPlace?.text = locationName
        textViewLocation?.text =
            getString(R.string.location, Utilities().todayDate(), latitude.toString(), longitude.toString(), currentTimeZone)

        textViewSunriseTime.text = Utilities().timeToZoneTime(sunResponse.results.sunrise, currentTimeZone)
        textViewSunsetTime.text = Utilities().timeToZoneTime(sunResponse.results.sunset, currentTimeZone)
        textViewFirstLight.text = getString(R.string.firstLight, Utilities().timeToZoneTime(sunResponse.results.civil_twilight_begin, currentTimeZone))
        textViewLastLight.text = getString(R.string.lastLight, Utilities().timeToZoneTime(sunResponse.results.civil_twilight_end, currentTimeZone))

        val (hours, minutes) = Utilities().timeModulusPair(sunResponse.results.day_length)
        textViewDayLength.text = getString(R.string.dayLengthText, hours, minutes)
    }
    //endregion

    private fun parseNominatim(list: List<NominatimResponse>) {
        if (list.isEmpty()) {
            textViewNotification?.text = getString(R.string.searchNotFound)
        } else {
            showBySearch = true
            val nominatimResponse = list.get(0)
            locationName = nominatimResponse.display_name
            showByRequest(nominatimResponse.lat.toDouble(), nominatimResponse.lon.toDouble())
        }
    }

    companion object {
        private const val TAG = "SunFragment"
    }
}