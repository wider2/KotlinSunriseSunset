package com.sunrisesunset.domain

import com.sunrisesunset.data.network.SunApi
import com.sunrisesunset.model.SunResponse
import com.sunrisesunset.provideSunApi
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SunUseCase {
    private val api: SunApi = provideSunApi()
    private val subscribeScheduler: Scheduler = Schedulers.io()
    private val observeScheduler: Scheduler = AndroidSchedulers.mainThread()

    operator fun invoke(lat: Double?, lng: Double?): Single<SunResponse> {
        return api.getByCoordinates(lat, lng, 0)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }
}