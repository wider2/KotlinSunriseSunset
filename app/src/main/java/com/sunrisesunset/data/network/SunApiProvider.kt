package com.sunrisesunset.data.network

import retrofit2.Retrofit

class SunApiProvider private constructor(private val retrofit: Retrofit) {

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
        private var INSTANCE: SunApiProvider? = null

        fun getInstance(retrofit: Retrofit) =
            INSTANCE ?: synchronized(DefaultSunClient::class) {
                INSTANCE ?: SunApiProvider(retrofit).also { INSTANCE = it }
            }
    }
}