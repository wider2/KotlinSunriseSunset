package com.sunrisesunset.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunrisesunset.Event
import com.sunrisesunset.SunriseApp
import com.sunrisesunset.data.dao.SunDatabase
import com.sunrisesunset.data.network.entityModel.EntityCity
import com.sunrisesunset.domain.NominatimUseCase
import com.sunrisesunset.model.NominatimResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NominatimViewModel(
    private val nominatimUseCase: NominatimUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var roomDb: SunDatabase = SunriseApp.sunriseApp.sunDatabase
    private lateinit var termQuery: String
    private var storedCityFound: Boolean = false

    init {
    }

    private val _showItemsError = MutableLiveData<Event<String>>()
    val showItemsError: LiveData<Event<String>> = _showItemsError

    private val _nominatimResponse = MutableLiveData<Event<List<NominatimResponse>>>()
    val nominatimResponse: LiveData<Event<List<NominatimResponse>>> = _nominatimResponse

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
        Log.i(TAG, "ViewModel destroyed")
    }

    //check for database storage
    //region DatabaseCheckStorageOfCity
    fun getCoordinatesByCity(term: String) {
        storedCityFound = false
        termQuery = term
        compositeDisposable.add(
            Observable.fromCallable<EntityCity> {
                val entityCity = roomDb.sunDatabaseDao.getByQuery(term)
                return@fromCallable entityCity
            }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    handleDatabaseResponse(it)
                }, {
                    handleDatabaseError(it)
                })
        )
    }
    private fun handleDatabaseError(error: Throwable) {
        if (error.message.equals("Callable returned null")) {
            storedCityFound = false
            getNominatimCity()
        } else {
            _showItemsError.value = Event(error.message.toString())
        }
    }

    private fun handleDatabaseResponse(entityCity: EntityCity?) {
        // if city stored in database - don't make internet request
        if (entityCity != null) {
            storedCityFound = true
            val list: MutableList<NominatimResponse> = mutableListOf()
            list.add(NominatimResponse(entityCity.lat.toString(), entityCity.lon.toString(), entityCity.city))
            handleResponse(list)
        } else {
            storedCityFound = false
            getNominatimCity()
        }
    }
    //endregion


    //if city absent in database
    //region NominatimCityResponse
    private fun getNominatimCity() {
        compositeDisposable.add(
            nominatimUseCase(termQuery)
                .subscribe({
                    handleResponse(it)
                }, {
                    handleError(it)
                })
        )
    }

    private fun handleResponse(nominatimResponse: List<NominatimResponse>) {
        if (!storedCityFound) dbInsertCity(nominatimResponse)
        _nominatimResponse.value = Event(nominatimResponse)
    }

    private fun handleError(error: Throwable) {
        _showItemsError.value = Event(error.localizedMessage)
    }
    //endregion


    // let's save city to local storage
    //region InsertCityToDatabase
    private fun dbInsertCity(nominatimResponse: List<NominatimResponse>) {
        compositeDisposable.add(
            Observable.fromCallable<Long> {
                var result: Long = 0L
                if (!nominatimResponse.isEmpty()) {
                    val nominatim = nominatimResponse.get(0)
                    val entity = EntityCity(
                        nominatim.lat.toDouble(),
                        nominatim.lon.toDouble(),
                        nominatim.display_name,
                        termQuery
                    )
                    result = roomDb.sunDatabaseDao.insertCity(entity)
                }
                return@fromCallable result
            }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    handleDatabaseInsertResponse(it)
                }, {
                    handleDatabaseInsertError(it)
                })
        )
    }
    private fun handleDatabaseInsertResponse(newReturnId: Long?) {
        Log.d(TAG, newReturnId.toString())
    }
    private fun handleDatabaseInsertError(error: Throwable) {
        _showItemsError.value = Event(error.localizedMessage)
    }
    //endregion

    companion object {
        private const val TAG = "NominatimViewModel"
    }
}