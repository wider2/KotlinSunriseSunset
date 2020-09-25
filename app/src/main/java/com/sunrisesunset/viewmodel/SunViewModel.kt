package com.sunrisesunset.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunrisesunset.Event
import com.sunrisesunset.domain.SunUseCase
import com.sunrisesunset.model.SunResponse
import io.reactivex.disposables.CompositeDisposable

class SunViewModel(
    private val sunUseCase: SunUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _showItemsError = MutableLiveData<Event<String>>()
    val showItemsError: LiveData<Event<String>> = _showItemsError

    private val _sunResponse = MutableLiveData<Event<SunResponse>>()
    val sunResponse: LiveData<Event<SunResponse>> = _sunResponse

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
        Log.i(TAG, "ViewModel destroyed")
    }

    //region SunDetails
    fun getSunDetailsByCoordinates(lat: Double?, lng: Double?) {
        compositeDisposable.add(
            sunUseCase(lat, lng)
                .subscribe({
                    handleResponse(it)
                }, {
                    handleError(it)
                })
        )
    }
    private fun handleResponse(sunResponse: SunResponse) {
        _sunResponse.value = Event(sunResponse)
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)
        _showItemsError.value = Event(error.localizedMessage)
    }
    //endregion

    companion object {
        private const val TAG = "SunViewModel"
    }
}