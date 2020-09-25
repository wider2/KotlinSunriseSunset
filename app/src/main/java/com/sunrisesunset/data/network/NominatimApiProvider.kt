package com.sunrisesunset.data.network

import retrofit2.Retrofit

class NominatimApiProvider private constructor(private val retrofit: Retrofit) {

    private var _api: SunApi? = null
    val api: SunApi
        get() {
            if (_api == null) {
                _api = retrofit.create(SunApi::class.java)
            }
            return _api ?: throw IllegalStateException("Api not initialized")
        }

    companion object {
        @Volatile
        private var INSTANCE: NominatimApiProvider? = null

        fun getInstance(retrofit: Retrofit) =
            INSTANCE ?: synchronized(DefaultSunClient::class) {
                INSTANCE ?: NominatimApiProvider(retrofit).also { INSTANCE = it }
            }
    }
}