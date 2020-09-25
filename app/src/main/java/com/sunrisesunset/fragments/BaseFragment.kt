package com.sunrisesunset.fragments

import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
     abstract fun updateMyCoordinates(lat: Double?, lng: Double?)
     abstract fun coordinatesAbsent(error: String)
}