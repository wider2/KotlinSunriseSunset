package com.sunrisesunset.data.network

import com.sunrisesunset.model.NominatimResponse
import com.sunrisesunset.model.SunResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SunApi {

    @GET("json")
    fun getByCoordinates(
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?,
        @Query("formatted") formatted: Int
    ): Single<SunResponse>

    @GET("search")
    fun getNominatimByCity(
        @Query("q") q: String?,
        @Query("format") format: String
    ): Single<List<NominatimResponse>>

}