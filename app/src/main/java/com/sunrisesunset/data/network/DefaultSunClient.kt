package com.sunrisesunset.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class DefaultSunClient {
    val okHttpClient: OkHttpClient

    private val interceptor: Interceptor = Interceptor { chain ->
        val request = chain.request()

        val connectionTimeout = chain.connectTimeoutMillis()
        val readTimeout = chain.readTimeoutMillis()
        val writeTimeout = chain.writeTimeoutMillis()

        val connectionTimeoutHeader = request.header(CONNECTION_TIMEOUT_HEADER)
        val readTimeoutHeader = request.header(READ_TIMEOUT_HEADER)
        val writeTimeoutHeader = request.header(WRITE_TIMEOUT_HEADER)

        val newConnectionTimeout = if (!connectionTimeoutHeader.isNullOrBlank()) {
            connectionTimeoutHeader.toIntOrNull() ?: connectionTimeout
        } else {
            connectionTimeout
        }

        val newReadTimeout = if (!readTimeoutHeader.isNullOrBlank()) {
            readTimeoutHeader.toIntOrNull() ?: readTimeout
        } else {
            readTimeout
        }

        val newWriteTimeout = if (!writeTimeoutHeader.isNullOrBlank()) {
            writeTimeoutHeader.toIntOrNull() ?: writeTimeout
        } else {
            writeTimeout
        }

        val builder = request.newBuilder()
            .removeHeader(CONNECTION_TIMEOUT_HEADER)
            .removeHeader(READ_TIMEOUT_HEADER)
            .removeHeader(WRITE_TIMEOUT_HEADER)

        val response = chain
            .withConnectTimeout(newConnectionTimeout, TimeUnit.MILLISECONDS)
            .withReadTimeout(newReadTimeout, TimeUnit.MILLISECONDS)
            .withWriteTimeout(newWriteTimeout, TimeUnit.MILLISECONDS)
            .proceed(builder.build())

        response
    }

    init {
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRIGHT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    companion object {
        const val BASE_URL = "https://api.sunrise-sunset.org/"
        const val BASE_URL_NOMINATIM = "https://nominatim.openstreetmap.org/"

        @Volatile
        private var INSTANCE: DefaultSunClient? = null

        fun getInstance() =
            INSTANCE ?: synchronized(DefaultSunClient::class) {
                INSTANCE ?: DefaultSunClient(
                ).also { INSTANCE = it }
            }
        private const val DEFAULT_CONNECTION_TIMEOUT = 10L
        private const val DEFAULT_READ_TIMEOUT = 10L
        private const val DEFAULT_WRIGHT_TIMEOUT = 10L

        const val CONNECTION_TIMEOUT_HEADER = "CONNECT_TIMEOUT"
        const val READ_TIMEOUT_HEADER = "READ_TIMEOUT"
        const val WRITE_TIMEOUT_HEADER = "WRITE_TIMEOUT"
    }
}