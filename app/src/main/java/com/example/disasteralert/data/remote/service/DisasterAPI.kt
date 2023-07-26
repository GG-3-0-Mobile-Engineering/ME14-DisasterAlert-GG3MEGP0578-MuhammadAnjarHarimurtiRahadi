package com.example.disasteralert.data.remote.service

import com.example.disasteralert.data.remote.response.DisasterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DisasterAPI {

    @GET("reports")
    suspend fun getAllDisasterData(): DisasterResponse

    @GET("reports")
    suspend fun getDisasterDataByLocation(
        @Query("admin") location: String
    ): DisasterResponse

    @GET("reports")
    suspend fun getDisasterDataByFilter(
        @Query("disaster") disasterFilter: String
    ): DisasterResponse
}