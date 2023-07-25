package com.example.disasteralert.data.remote.service

import com.example.disasteralert.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DisasterServiceAPI {

    private const val BASE_URL = "https://data.petabencana.id/"

    fun getApiService(): DisasterAPI {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .build()
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        return retrofit.create(DisasterAPI::class.java)
    }
}