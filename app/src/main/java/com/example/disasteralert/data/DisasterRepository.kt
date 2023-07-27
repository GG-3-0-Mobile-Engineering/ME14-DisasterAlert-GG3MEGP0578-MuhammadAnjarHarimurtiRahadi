package com.example.disasteralert.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.disasteralert.data.remote.response.disasterresponse.DisasterResponse
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.helper.Util

class DisasterRepository private constructor(
    private val apiService: DisasterAPI,
) {

    fun getAllDisasterData(
        locFilter: String, disasterFilter: String, startDate: String, endDate: String
    ): LiveData<Results<DisasterResponse>> = liveData {
        emit(Results.Loading)
        try {
            if (disasterFilter.isNotBlank()) {
                val response = apiService.getDisasterDataByFilter(
                    disasterFilter = disasterFilter.lowercase()
                )
                emit(Results.Success(response))
            } else if (locFilter.isNotBlank() && startDate.isEmpty()) {
                val response = apiService.getDisasterDataByLocation(
                    location = locFilter
                )
                emit(Results.Success(response))
            } else if (locFilter.isNotBlank() || (startDate.isNotBlank() && endDate.isNotBlank())) {
                val response = apiService.getDisasterDataByPeriod(
                    startDate = Util.getDateApiFormat(startDate),
                    endDate = Util.getDateApiFormat(endDate),
                    location = locFilter
                )
                emit(Results.Success(response))
            } else  {
                val response = apiService.getAllDisasterData()
                emit(Results.Success(response))
            }

        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: DisasterRepository? = null
        fun getInstance(
            apiService: DisasterAPI
        ): DisasterRepository = instance ?: synchronized(this) {
            instance ?: DisasterRepository(apiService)
        }.also { instance = it }
    }
}