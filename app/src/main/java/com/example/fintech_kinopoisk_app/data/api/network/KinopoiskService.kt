package com.example.fintech_kinopoisk_app.data.api.network

import com.example.fintech_kinopoisk_app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KinopoiskService {
    private var okHttpClient: OkHttpClient? = null
    private var retrofitInstance: KinopoiskApiService? = null

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().build()
                chain.proceed(request)
            }
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return client.build()
    }

    private fun createRetrofitInstance(): KinopoiskApiService {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BuildConfig.KINOPOISK_API_URL)
            .client(okHttpClient ?: OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitBuilder.create(KinopoiskApiService::class.java)
    }

    private fun getRetrofitInstance() {
        if (okHttpClient == null) {
            okHttpClient = getOkHttpClient()
        }
        retrofitInstance = createRetrofitInstance()
    }

    fun getInstance(): KinopoiskApiService? {
        return if (retrofitInstance == null) {
            getRetrofitInstance()
            retrofitInstance
        } else retrofitInstance
    }
}