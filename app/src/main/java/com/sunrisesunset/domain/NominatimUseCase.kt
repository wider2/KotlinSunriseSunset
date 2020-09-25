package com.sunrisesunset.domain

import com.sunrisesunset.data.network.SunApi
import com.sunrisesunset.model.NominatimResponse
import com.sunrisesunset.provideNominatimApi
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NominatimUseCase {
    private val api: SunApi = provideNominatimApi()
    private val subscribeScheduler: Scheduler = Schedulers.io()
    private val observeScheduler: Scheduler = AndroidSchedulers.mainThread()

    operator fun invoke(q: String?): Single<List<NominatimResponse>> {

        return api.getNominatimByCity(q, "json")
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }
}