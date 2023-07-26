package com.example.disasteralert.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.disasteralert.data.remote.response.DisasterResponse
import com.example.disasteralert.data.remote.service.DisasterAPI
import com.example.disasteralert.helper.Util

class DisasterRepository private constructor(
    private val apiService: DisasterAPI,
) {

    fun getAllDisasterData(
        locFilter: String, disasterFilter: String
    ): LiveData<Results<DisasterResponse>> = liveData {
        emit(Results.Loading)
        try {
            if (locFilter.isBlank() && disasterFilter.isBlank()) {
                val response = apiService.getAllDisasterData()
                emit(Results.Success(response))
            } else if (locFilter.isNotBlank()) {
                val response = apiService.getDisasterDataByLocation(
                    start = Util.getStartDate(), end = Util.getEndDate(), location = locFilter
                )
                emit(Results.Success(response))
            } else if (disasterFilter.isNotBlank()) {
                val response = apiService.getDisasterDataByFilter(
                    disasterFilter = disasterFilter.lowercase()
                )
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