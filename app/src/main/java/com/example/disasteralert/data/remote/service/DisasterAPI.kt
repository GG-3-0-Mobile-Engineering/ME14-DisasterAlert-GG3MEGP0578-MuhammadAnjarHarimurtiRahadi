package com.example.disasteralert.data.remote.service

import com.example.disasteralert.data.remote.response.disasterresponse.DisasterResponse
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResponse
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

    @GET("reports/archive")
    suspend fun getDisasterDataByPeriod(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("admin") location: String
    ): DisasterResponse

    @GET("floodgauges")
    suspend fun getFloodGaugesData(): FloodGaugesResponse
}