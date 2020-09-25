package com.sunrisesunset.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunrisesunset.domain.NominatimUseCase

@Suppress("UNCHECKED_CAST")
class NominatimViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NominatimViewModel::class.java)) {
            return NominatimViewModel(
                NominatimUseCase()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}