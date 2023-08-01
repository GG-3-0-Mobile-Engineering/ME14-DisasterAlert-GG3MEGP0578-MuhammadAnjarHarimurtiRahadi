package com.example.disasteralert.data.remote.service

import com.example.disasteralert.data.remote.response.disasterresponse.DisasterResponse
import com.example.disasteralert.data.remote.response.floodgaugesresponse.FloodGaugesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DisasterAPI {

    @GET("reports")
    suspend fun getAllDisasterData(): DisasterResponse

    @GET("reports/archive")
    suspend fun getDisasterDataByPeriod(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
    ): DisasterResponse

    @GET("floodgauges")
    suspend fun getFloodGaugesData(): FloodGaugesResponse
}