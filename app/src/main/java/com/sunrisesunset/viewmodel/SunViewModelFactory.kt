package com.sunrisesunset.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunrisesunset.domain.SunUseCase

@Suppress("UNCHECKED_CAST")
class SunViewModelFactory(

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SunViewModel::class.java)) {
            return SunViewModel(
                SunUseCase()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}