package com.sunrisesunset

import com.sunrisesunset.data.network.DefaultSunClient
import com.sunrisesunset.data.network.NominatimApiProvider
import com.sunrisesunset.data.network.SunApi
import com.sunrisesunset.data.network.SunApiProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun provideRetrofit(
    okHttpClient: OkHttpClient = provideDefaultSunClient().okHttpClient,
    baseUrl: String = DefaultSunClient.BASE_URL
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun provideDefaultSunClient(): DefaultSunClient {
    return DefaultSunClient.getInstance()
}

fun provideSunApi(): SunApi {
    return SunApiProvider.getInstance(provideRetrofit()).api
}

fun provideNominatimApi(): SunApi {
    return NominatimApiProvider.getInstance(provideRetrofitNominatim()).api
}

fun provideRetrofitNominatim(
    okHttpClient: OkHttpClient = provideDefaultSunClient().okHttpClient,
    baseUrl: String = DefaultSunClient.BASE_URL_NOMINATIM
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}
